using System;
using System.Threading;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;

namespace SiWatchApp
{
    public class AppPage : TwoButtonPage
    {
        public SynchronizationContext synchronizationContext;

        public AppPage()
        {
            synchronizationContext = SynchronizationContext.Current;
        }

        protected void Invoke(Action action)
        {
            if (Device.IsInvokeRequired) {
                Device.BeginInvokeOnMainThread(action);
            }
            else {
                action();
            }
        }
    }
}
