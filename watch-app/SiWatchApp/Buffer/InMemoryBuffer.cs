using System;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;

namespace SiWatchApp.Buffer
{
    public class InMemoryBuffer<T> : IBuffer<T> where T : class
    {
        private class BufferEntry
        {
            public T Item { get; }
            public bool IsLocked { get; set; } = false;

            public BufferEntry(T item)
            {
                Item = item;
            }
        }

        private class BufferBlock : IBlock<T>
        {
            public static readonly BufferBlock Empty = new BufferBlock();

            private readonly object _sync = new object();

            private ICollection<LinkedListNode<BufferEntry>> _nodes;

            private BufferBlock()
            {
                _nodes = null;
            }

            public BufferBlock(ICollection<LinkedListNode<BufferEntry>> nodes)
            {
                _nodes = nodes;
            }

            public ICollection<T> Items
            {
                get {
                    if (_nodes != null) {
                        lock (_sync) {
                            if (_nodes != null) {
                                return _nodes.Select(n => n.Value.Item).ToList();
                            }
                        }
                    }
                    return null;
                }
            }
            
            public void Discard()
            {
                lock (_sync) {
                    if (_nodes == null)
                        return;

                    foreach (var node in _nodes) {
                        lock (node.List) {
                            node.List.Remove(node);
                        }
                    }

                    _nodes = null;
                }
            }

            public bool IsEmpty
            {
                get {
                    lock (_sync) {
                        return _nodes == null || _nodes.Count == 0;
                    }
                }
            }

            public void Return()
            {
                lock (_sync) {
                    if (_nodes == null)
                        return;

                    foreach (var node in _nodes) {
                        lock (node.List) {
                            node.Value.IsLocked = false;
                        }
                    }

                    _nodes = null;
                }
            }
        }
        
        private readonly ImmutableSortedDictionary<Priority, LinkedList<BufferEntry>> _buffers;

        public InMemoryBuffer()
        {
            _buffers = Enum.GetValues(typeof(Priority)).Cast<Priority>()
                .ToImmutableSortedDictionary(p => p, p => new LinkedList<BufferEntry>());
        }

        public void Append(T item, Priority priority)
        {
            if(item == null)
                throw new ArgumentNullException(nameof(item));

            var list = _buffers[priority];
            lock (list)
            {
                list.AddLast(new BufferEntry(item));
            }
        }

        public void AppendAll(IEnumerable<T> items, Priority priority)
        {
            if (items == null)
                throw new ArgumentNullException(nameof(items));

            var list = _buffers[priority];
            lock (list)
            {
                foreach (var item in items) {
                    list.AddLast(new BufferEntry(item));
                }
            }
        }

        public T Take()
        {
            foreach (var list in _buffers.Values) {
                lock (list)
                {
                    var node = list.First;
                    while (node != null && node.Value.IsLocked) {
                        node = node.Next;
                    }
                    if (node != null) {
                        list.Remove(node);
                        return node.Value.Item;
                    }
                }
            }
            return null;
        }

        public ICollection<T> Take(int count)
        {
            var items = new List<T>(count);
            foreach (var list in _buffers.Values) {
                lock (list) {
                    var node = list.First;
                    while (node != null) {
                        if (!node.Value.IsLocked) {
                            list.Remove(node);
                            items.Add(node.Value.Item);
                            if (items.Count == count)
                                return items;
                        }
                        node = node.Next;
                    }
                }
            }
            return items;
        }

        public IBlock<T> Get(int count)
        {
            var nodes = new List<LinkedListNode<BufferEntry>>(count);
            foreach (var list in _buffers.Values) {
                lock (list) {
                    var node = list.First;
                    while (node != null) {
                        if (!node.Value.IsLocked) {
                            node.Value.IsLocked = true;
                            nodes.Add(node);
                            if (nodes.Count == count)
                                goto getout;
                        }
                        node = node.Next;
                    }
                }
            }
        getout:
            return new BufferBlock(nodes);
        }

        public int Count
        {
            get {
                return _buffers.Values.Sum(list => { lock (list) { return list.Count; } });
            }
        }

        public void Dispose()
        {
            foreach (var list in _buffers.Values) {
                list.Clear();
            }
        }
    }
}
