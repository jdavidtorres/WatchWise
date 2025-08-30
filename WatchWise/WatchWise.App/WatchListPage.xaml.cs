using System.Collections.ObjectModel;
using System.Text.Json;
using System.Linq;
using Microsoft.Maui.Storage;
using WatchWise.App.Models;

namespace WatchWise.App;

public partial class WatchListPage : ContentPage
{
    private const string WatchListKey = "watchlist";
    private readonly ObservableCollection<WatchItem> _items = new();

    public WatchListPage()
    {
        InitializeComponent();
        WatchListView.ItemsSource = _items;
    }

    protected override void OnAppearing()
    {
        base.OnAppearing();
        var json = Preferences.Get(WatchListKey, string.Empty);
        if (!string.IsNullOrEmpty(json))
        {
            try
            {
                var loaded = JsonSerializer.Deserialize<List<WatchItem>>(json);
                if (loaded != null)
                {
                    _items.Clear();
                    foreach (var item in loaded)
                        _items.Add(item);
                }
            }
            catch
            {
                // ignore deserialize errors
            }
        }
    }

    protected override void OnDisappearing()
    {
        base.OnDisappearing();
        var json = JsonSerializer.Serialize(_items.ToList());
        Preferences.Set(WatchListKey, json);
    }

    private void OnAddItem(object sender, EventArgs e)
    {
        var title = NewItemEntry.Text?.Trim();
        if (!string.IsNullOrEmpty(title))
        {
            _items.Add(new WatchItem { Title = title });
            NewItemEntry.Text = string.Empty;
        }
    }

    private void OnRemoveItem(object sender, EventArgs e)
    {
        if ((sender as Button)?.CommandParameter is WatchItem item)
            _items.Remove(item);
    }
}
