using System;
using Xamarin.Forms;

namespace SiWatchApp
{
    public class StartPage : AppPage
    {
        public StartPage()
        {
            var titleLabel = new Label() {
                    Text = "SiWatch",
                    HorizontalTextAlignment = TextAlignment.Center
            };
            var button = new Button() {
                    Text = "Start",
                    HorizontalOptions = LayoutOptions.Center
            };
            button.Clicked += Button_Clicked;
            Content = new StackLayout() {
                    VerticalOptions = LayoutOptions.Center,
                    Children = { titleLabel, button }
            };
        }

        public event EventHandler Start;

        private void Button_Clicked(object sender, System.EventArgs e)
        {
            Start?.Invoke(this, EventArgs.Empty);    
        }
    }
}
