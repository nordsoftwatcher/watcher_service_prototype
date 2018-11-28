using System;
using SiWatchApp.UI;
using Tizen.Wearable.CircularUI.Forms;
using Xamarin.Forms;

namespace SiWatchApp
{
    public class MainPage : AppPage
    {
        private Label titleLabel = new Label() { FontSize = 7, FontAttributes = FontAttributes.Bold, TextColor = Color.MediumPurple, HorizontalOptions = LayoutOptions.Center, Text = "SiWatch"};
        private Label policyLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Label apiUrlLabel = new Label() { FontSize = 4, HorizontalOptions = LayoutOptions.Center };
        private Label timeLabel = new Label() { FontSize = 8, TextColor = Color.LawnGreen, HorizontalOptions = LayoutOptions.Center };
        private Button sosButton = new Button() { FontSize = 10, WidthRequest = 180, BackgroundColor = Color.Red, Text = "    SOS    ", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Button startFinishButton = new Button() { FontSize = 10, WidthRequest = 180, BackgroundColor = Color.Blue, Text = "Action", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Label statusLabel = new Label() { FontSize = 8, TextColor = Color.DeepPink, HorizontalOptions = LayoutOptions.Center };
        private Label locationLabel = new Label() { FontSize = 6, TextColor = Color.LightSeaGreen, HorizontalOptions = LayoutOptions.Center };
        private Label bufferLabel = new Label() { FontSize = 5, TextColor = Color.SandyBrown, HorizontalOptions = LayoutOptions.Center };
        //private Button exitButton = new Button() { FontSize = 9, HorizontalOptions = LayoutOptions.Center, Text = "Exit", IsEnabled = true};

        public MainPage()
        {
            var layout = new StackLayout() {
                    Spacing = 3,
                    VerticalOptions = LayoutOptions.Center,
                    Children = { titleLabel, policyLabel, apiUrlLabel, sosButton, statusLabel, startFinishButton, locationLabel, bufferLabel, timeLabel }
            };
            Content = layout;
            FirstButton = new MenuItem() { Icon = Icons.Exit };
            FirstButton.Clicked += (sender, args) => ExitRequest?.Invoke(this, EventArgs.Empty);
            
            sosButton.Clicked += (sender, args) => SOSRequest?.Invoke(this, EventArgs.Empty);
            //exitButton.Clicked += (sender, args) => ExitRequest?.Invoke(this, EventArgs.Empty);
            startFinishButton.Clicked += (sender, args) => StartFinishRequest?.Invoke(this, EventArgs.Empty);
        }

        public void ShowMessageButton(bool show)
        {

            if (show) {
                if (SecondButton == null) {
                    SecondButton = new MenuItem() { Icon = Icons.Message };
                    SecondButton.Clicked += OnReadMessageRequest;
                }
            }
            else {
                if (SecondButton != null) {
                    SecondButton.Clicked -= OnReadMessageRequest;
                    SecondButton = null;
                }
            }
        }

        private void OnReadMessageRequest(object sender, EventArgs e)
        {
            ReadMessageRequest?.Invoke(this, EventArgs.Empty);
        }

        public void SetPolicyInfo(String info)
        {
            policyLabel.Text = info ?? "";
        }

        public void SetApiUrl(String apiUrl)
        {
            apiUrlLabel.Text = apiUrl ?? "<UNKNOWN>";
        }

        public void SetTime(String time)
        {
            timeLabel.Text = time;
        }

        public void SetStatus(String status)
        {
            statusLabel.Text = status ?? "";
        }

        public void SetBufferInfo(String info)
        {
            bufferLabel.Text = info ?? "";
        }

        public void SetLocationInfo(String info)
        {
            locationLabel.Text = info ?? "";
        }
        
        public void EnableSOS(bool enabled)
        {
            sosButton.IsVisible = enabled;
        }
        
        public void EnableStartFinish(bool enabled)
        {
            startFinishButton.IsVisible = enabled;
        }

        public void SetStartFinishText(string text)
        {
            startFinishButton.Text = text;
        }

        public event EventHandler ExitRequest;
        public event EventHandler SOSRequest;
        public event EventHandler StartFinishRequest;
        public event EventHandler ReadMessageRequest;
    }
}
