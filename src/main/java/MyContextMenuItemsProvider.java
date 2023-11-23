import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.Http;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.sitemap.SiteMap;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyContextMenuItemsProvider implements ContextMenuItemsProvider
{
    private final Http http;
    private final SiteMap siteMap;
    private final ExecutorService executorService;

    public MyContextMenuItemsProvider(Http http, SiteMap siteMap, ExecutorService executorService)
    {
        this.http = http;
        this.siteMap = siteMap;
        this.executorService = executorService;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event)
    {
        List<Component> components = new ArrayList<>();

        if (event.isFromTool(ToolType.TARGET))
        {
            JMenuItem item = new JMenuItem("Fetch empty response");
            item.addActionListener(l -> {
                List<HttpRequestResponse> requestResponses = event.selectedRequestResponses();

                for (HttpRequestResponse requestResponse : requestResponses)
                {
                    if (requestResponse.response() == null)
                    {
                        executorService.execute(() -> {
                            HttpRequestResponse issuedRequestResponse = http.sendRequest(requestResponse.request());

                            siteMap.add(issuedRequestResponse);
                        });
                    }
                }
            });

            components.add(item);
        }

        return components;
    }
}
