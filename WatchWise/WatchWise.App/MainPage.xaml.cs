using Microsoft.Maui.Controls;
using System;
using WatchWise.App.Services;

namespace WatchWise.App
{
    public partial class MainPage : ContentPage
    {
        private readonly TitleSearchService _titleSearchService = new();
        int count = 0;

        public MainPage()
        {
            InitializeComponent();
        }

        private void OnCounterClicked(object sender, EventArgs e)
        {
            count++;

            if (count == 1)
                CounterBtn.Text = $"Clicked {count} time";
            else
                CounterBtn.Text = $"Clicked {count} times";

            SemanticScreenReader.Announce(CounterBtn.Text);
        }

        private async void OnSearchClicked(object? sender, EventArgs e)
        {
            var query = QueryEntry.Text;
            if (string.IsNullOrWhiteSpace(query))
            {
                ResultLabel.Text = "Please enter a search query.";
                return;
            }

            ResultLabel.Text = "Searching...";
            try
            {
                var result = await _titleSearchService.SearchAsync(query);
                ResultLabel.Text = result;
            }
            catch (Exception ex)
            {
                ResultLabel.Text = ex.Message;
            }
        }

        private async void OnOpenWatchListClicked(object? sender, EventArgs e)
        {
            await Shell.Current.GoToAsync(nameof(WatchListPage));
        }
    }
}
