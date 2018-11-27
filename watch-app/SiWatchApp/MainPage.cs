using System;
using Xamarin.Forms;

namespace SiWatchApp
{
    public class MainPage : AppPage
    {
        private Label titleLabel = new Label() { FontSize = 7, TextColor = Color.LightCyan, HorizontalOptions = LayoutOptions.Center, Text = "SiWatch"};
        private Label policyLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Label apiUrlLabel = new Label() { FontSize = 5, HorizontalOptions = LayoutOptions.Center };
        private Button sosButton = new Button() { FontSize = 10, BackgroundColor = Color.Red, Text = " SOS ", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Button startFinishButton = new Button() { FontSize = 10, WidthRequest = 180, BackgroundColor = Color.Blue, Text = "Action", HorizontalOptions = LayoutOptions.Center, IsVisible = false };
        private Label statusLabel = new Label() { FontSize = 7, TextColor = Color.DeepPink, HorizontalOptions = LayoutOptions.Center };
        private Label locationLabel = new Label() { FontSize = 5, TextColor = Color.LightSeaGreen, HorizontalOptions = LayoutOptions.Center };
        private Label bufferLabel = new Label() { FontSize = 5, TextColor = Color.DeepSkyBlue, HorizontalOptions = LayoutOptions.Center };
        private Button exitButton = new Button() { FontSize = 9, HorizontalOptions = LayoutOptions.Center, Text = "Exit", IsEnabled = true};

        public MainPage()
        {
            var layout = new StackLayout() {
                    Spacing = 4,
                    VerticalOptions = LayoutOptions.Center,
                    Children = { titleLabel, policyLabel, apiUrlLabel, sosButton, statusLabel, startFinishButton,  locationLabel, bufferLabel, exitButton }
            };
            Content = layout;

            sosButton.Clicked += (sender, args) => SOSRequest?.Invoke(this, EventArgs.Empty);
            exitButton.Clicked += (sender, args) => ExitRequest?.Invoke(this, EventArgs.Empty);
            startFinishButton.Clicked += (sender, args) => StartFinishRequest?.Invoke(this, EventArgs.Empty);
        }

        public void SetPolicyInfo(String info)
        {
            Invoke(() => policyLabel.Text = info ?? "");
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
