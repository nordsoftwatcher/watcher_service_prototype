using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Linq;
using System.Text;
using SiWatchApp.Configuration;
using SiWatchApp.Services;
using Tizen.Sensor;
using Xamarin.Forms;
using Tizen.Wearable.CircularUI.Forms;

namespace SiWatchApp
{
    public class App : Application
    {
        private StartPage _startPage;
        private MainPage _mainPage;

        public App()
        {
            _startPage = new StartPage();
            _startPage.Start += _startPage_Start;

            _mainPage = new MainPage(new SettingsService());

            MainPage = _startPage;
        }

        private async void _startPage_Start(object sender, EventArgs e)
        {
            await _startPage.Navigation.PushModalAsync(_mainPage);
            await _mainPage.Init();
        }
        
        
        //private async void Button_Clicked(object sender, EventArgs e)
        //{
        //    try
        //    {
        //        System.Net.Http.HttpClient httpClient = new System.Net.Http.HttpClient();
        //        var response = await httpClient.GetAsync("http://10.0.2.2:8088/welcome.html");
        //        var content = await response.Content.ReadAsStringAsync();
        //        label.Text = content;
        //    }
        //    catch (Exception ex) {
        //        label.Text = ex.Message;
        //    }
        //}

        //private async void Button_Clicked(object sender, EventArgs e)
        //{
        //    if (Tizen.Sensor.Gyroscope.IsSupported)
        //    {
        //        Tizen.Sensor.Gyroscope gyr = new Tizen.Sensor.AccelerometerDataUpdatedEventArgs().Gyroscope();
        //        var gyro = Observable.FromEventPattern<GyroscopeDataUpdatedEventArgs>(
        //                h => gyr.DataUpdated += h, h => gyr.DataUpdated -= h);

        //        gyro.a
                
        //        //label.Text = $"X={gyr.X},Y={gyr.Y}";
        //        gyr.DataUpdated += (s, ev) =>
        //        {
        //            label.Text = $"X={ev.X},Y={ev.Y}";
        //        };
        //        gyr.Start();
        //    }
        //    else {
        //        label.Text = "Gyr not supported";
        //    }
        //}

        protected override void OnStart()
        {
            // Handle when your app starts
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
        }

        protected override void OnResume()
        {
            // Handle when your app resumes
        }
    }
}
