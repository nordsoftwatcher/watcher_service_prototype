using System;
using System.Threading;
using System.Threading.Tasks;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;

namespace SiWatchApp.UI
{
    public static class Notification
    {
        private static SynchronizationContext sc;

        public static void Init()
        {
            sc = SynchronizationContext.Current;
        }

        public static void ShowToast(string text, TimeSpan duration)
        {
            sc.Post(_ => { Toast.DisplayText(text, (int)duration.TotalMilliseconds ); }, null);
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
                                              popup.Dismiss();
                                              tcs.SetResult(true);
                                          };
            popup.BackButtonPressed += (sender, args) => {
                                           popup.Dismiss();
                                           tcs.SetResult(false);
                                       };
            //popup.Show();
            //Device.BeginInvokeOnMainThread(() => popup.Show());
            sc.Post(_ => popup.Show(), null);

            return tcs.Task;
        }

        public static Task<bool?> ShowQuestion(string question, string yes = "Yes", string no = "No")
        {
            TaskCompletionSource<bool?> tcs = new TaskCompletionSource<bool?>();

            var popup = new TwoButtonPopup {
                    Title = "Question",
                    FirstButton = new MenuItem() { Text = no, Icon = new FileImageSource { File = "no.png", }, },
                    SecondButton = new MenuItem() { Text = yes, Icon = new FileImageSource { File = "yes.png", }, },
                    Content = new Label() {  Text = question, VerticalOptions = LayoutOptions.Center, HorizontalOptions = LayoutOptions.Center}
            };
            popup.FirstButton.Clicked += (sender, args) => {
                                              popup.Dismiss();
                                              tcs.SetResult(false);
                                          };
            popup.SecondButton.Clicked += (sender, args) => {
                                             popup.Dismiss();
                                             tcs.SetResult(true);
                                         };
            popup.BackButtonPressed += (sender, args) => {
                                           popup.Dismiss();
                                           tcs.SetResult(null);
                                       };

            //popup.Show();
            //Device.BeginInvokeOnMainThread(() => popup.Show());
            sc.Post(_ => popup.Show(), null);

            return tcs.Task;
        }
    }
}
