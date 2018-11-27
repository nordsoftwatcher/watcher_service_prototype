using System;
using System.Threading;
using System.Threading.Tasks;
using Tizen.Applications;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;
using Application = Tizen.Applications.Application;

namespace SiWatchApp.UI
{
    public static class Notification
    {
        private static SynchronizationContext sc;

        public static void Init()
        {
            sc = SynchronizationContext.Current;
        }

        private static void Invoke(Action action)
        {
            if (Device.IsInvokeRequired) {
                Device.BeginInvokeOnMainThread(action);
            }
            else {
                action();
            }
        }

        public static void ShowToast(string text, TimeSpan duration)
        {
            Invoke(() => { Toast.DisplayText(text, (int)duration.TotalMilliseconds ); });
        }

        public static Task<bool> ShowInfo(string title, string text)
        {
            TaskCompletionSource<bool> tcs = new TaskCompletionSource<bool>();

            var popup = new InformationPopup() {
                    Title = title,
                    Text = text,
                    IsProgressRunning = false,
                    BottomButton = new MenuItem { Text = "OK" }
            };
            popup.BottomButton.Clicked += (sender, args) => {
                                              Invoke(() => popup.Dismiss());
                                              tcs.SetResult(true);
                                          };
            popup.BackButtonPressed += (sender, args) => {
                                           Invoke(() => popup.Dismiss());
                                           tcs.SetResult(false);
                                       };

            Invoke(() => popup.Show());
            
            return tcs.Task;
        }

        public static void ShowQuestion(string question, Action<bool?> callback, string yes = "Yes", string no = "No")
        {
            var popup = new TwoButtonPopup {
                    //Title = "Question",
                    FirstButton = new MenuItem() { Text = no, Icon = new FileImageSource { File = "no.png", }, },
                    SecondButton = new MenuItem() { Text = yes, Icon = new FileImageSource { File = "yes.png", }, },
                    Content = new Label() {  Text = question, VerticalOptions = LayoutOptions.Center, HorizontalOptions = LayoutOptions.Center}
            };
            popup.FirstButton.Clicked += (sender, args) => {
                                              popup.Dismiss();
                                              callback(false);
                                          };
            popup.SecondButton.Clicked += (sender, args) => {
                                             popup.Dismiss();
                                             callback(true);
                                         };
            popup.BackButtonPressed += (sender, args) => {
                                           popup.Dismiss();
                                           callback(null);
                                       };

            Invoke(() => popup.Show());
        }
    }
}
