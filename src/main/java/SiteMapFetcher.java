import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SiteMapFetcher implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi api)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        api.extension().setName("Site map fetcher");

        api.userInterface().registerContextMenuItemsProvider(new MyContextMenuItemsProvider(api.http(), api.siteMap(), executorService));

        api.extension().registerUnloadingHandler(executorService::shutdownNow);
    }
}