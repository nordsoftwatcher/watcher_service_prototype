using System;
using System.Collections.Generic;

namespace SiWatchApp.Utils
{
    public static class EnumerableExtensions
    {
        public static IDictionary<TKey, List<TValue>> GroupToDictionary<TSource, TKey, TValue>(
                this IEnumerable<TSource> enumeration, Func<TSource, TKey> keySelector, Func<TSource, TValue> valueSelector)
        {
            Dictionary<TKey, List<TValue>> dictionary = new Dictionary<TKey, List<TValue>>();
            foreach (TSource source in enumeration)
            {
                TKey key = keySelector(source);
                TValue value = valueSelector(source);
                if (!dictionary.ContainsKey(key))
                    dictionary.Add(key, new List<TValue>() { value });
                else
                    dictionary[key].Add(value);
            }
            return dictionary;
        }
    }
}
