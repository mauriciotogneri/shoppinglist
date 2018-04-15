package com.mauriciotogneri.shoppinglist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.mauriciotogneri.shoppinglist.R;

import java.util.List;

public class TestActivity extends Activity implements MessageClient.OnMessageReceivedListener
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_test);

        findViewById(R.id.test).setOnClickListener(v -> sendMessage());
    }

    private void sendMessage()
    {
        Task<List<Node>> task = Wearable.getNodeClient(this).getConnectedNodes();
        task.addOnSuccessListener(nodes ->
        {
            for (Node node : nodes)
            {
                Wearable.getMessageClient(this).sendMessage(node.getId(), "aaa", new byte[] {1, 2, 3});
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        Wearable.getMessageClient(this).removeListener(this);
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent)
    {
        Toast.makeText(this, messageEvent.getPath() + messageEvent.getData().length, Toast.LENGTH_SHORT).show();
    }
}