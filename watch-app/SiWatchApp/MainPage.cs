using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using SiWatchApp.Events;
using SiWatchApp.Services;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;

namespace SiWatchApp
{
    public class MainPage : AppPage
    {
        private Label titleLabel = new Label() { FontSize = 7, TextColor = Color.LightCyan, HorizontalOptions = LayoutOptions.Center, Text = "SiWatch"};
        private Label deviceIdLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Label apiUrlLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Button sosButton = new Button() { FontSize = 12, BackgroundColor = Color.Red, Text = "SOS", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Button startFinishButton = new Button() { FontSize = 9, BackgroundColor = Color.Blue, Text = "Action", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Label statusLabel = new Label() { FontSize = 5, TextColor = Color.DeepPink, HorizontalOptions = LayoutOptions.Center };
        private Label locationLabel = new Label() { FontSize = 5, TextColor = Color.LightSeaGreen, HorizontalOptions = LayoutOptions.Center };
        private Label bufferLabel = new Label() { FontSize = 5, TextColor = Color.DeepSkyBlue, HorizontalOptions = LayoutOptions.Center };
        private Button exitButton = new Button() { FontSize = 9, HorizontalOptions = LayoutOptions.Center, Text = "Exit", IsEnabled = true};

        public MainPage()
        {
            var layout = new StackLayout() {
                    Spacing = 4,
                    VerticalOptions = LayoutOptions.Center,
                    Children = { titleLabel, deviceIdLabel, apiUrlLabel, sosButton, startFinishButton, statusLabel, locationLabel, bufferLabel, exitButton }
            };
            Content = layout;

            sosButton.Clicked += (sender, args) => SOSRequest?.Invoke(this, EventArgs.Empty);
            exitButton.Clicked += (sender, args) => ExitRequest?.Invoke(this, EventArgs.Empty);
            startFinishButton.Clicked += (sender, args) => StartFinishRequest?.Invoke(this, EventArgs.Empty);
        }

        public void SetDeviceId(String deviceId)
        {
            Invoke(() => deviceIdLabel.Text = deviceId ?? "<UNKNOWN>");
        }

        public void SetApiUrl(String apiUrl)
        {
            Invoke(() => apiUrlLabel.Text = apiUrl ?? "<UNKNOWN>");
        }

        public void SetStatus(String status)
        {
            Invoke(() => statusLabel.Text = status ?? "");
        }

        public void SetBufferInfo(String info)
        {
            Invoke(() => bufferLabel.Text = info ?? "");
        }

        public void SetLocationInfo(String info)
        {
            Invoke(() => locationLabel.Text = info ?? "");
        }
        

        public void EnableSOS(bool enabled)
        {
            Invoke(() => sosButton.IsVisible = enabled);
        }
        
        public void EnableStartFinish(bool enabled)
        {
            Invoke(() => startFinishButton.IsVisible = enabled);
        }

        public void SetStartFinishText(string text)
        {
            Invoke(() => startFinishButton.Text = text);
        }

        public event EventHandler ExitRequest;
        public event EventHandler SOSRequest;
        public event EventHandler StartFinishRequest;
    }
}
