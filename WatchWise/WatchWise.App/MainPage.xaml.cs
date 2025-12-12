using Microsoft.Maui.Controls;
using WatchWise.App.Services;

namespace WatchWise.App;

public partial class MainPage : ContentPage
{
    private readonly TitleSearchService _titleSearchService = new();

    public MainPage()
    {
        InitializeComponent();
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
}
