using System;

namespace SiWatchApp
{
    class Program : global::Xamarin.Forms.Platform.Tizen.FormsApplication
    {
        protected override void OnCreate()
        {
            base.OnCreate();

            var app = new App();
            LoadApplication(app);

            app.Init();
        }

        static void Main(string[] args)
        {
            var program = new Program();
            global::Xamarin.Forms.Platform.Tizen.Forms.Init(program);
            global::Tizen.Wearable.CircularUI.Forms.Renderer.FormsCircularUI.Init();
            program.Run(args);
        }
    }
}
