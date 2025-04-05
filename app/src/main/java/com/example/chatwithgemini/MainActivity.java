package com.example.chatwithgemini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "";
    private static final String MODEL_NAME = "gemini-2.0-flash-001";

    private EditText messageInput;
    private Button sendButton;
    private TextView responseText;
    private GenerativeModelFutures generativeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        responseText = findViewById(R.id.response_text);

        // Initialize Gemini model
        GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);
        generativeModel = GenerativeModelFutures.from(model);

        // Set click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage(String message) {
        // Show loading state
        responseText.setText("Thinking...");

        // Create content with user's message
        Content content = new Content.Builder()
                .addText(message)
                .build();

        // Generate response from Gemini
        ListenableFuture<GenerateContentResponse> response =
                generativeModel.generateContent(content);

        // Handle the response
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        runOnUiThread(() -> {
                            // Display the response
                            String responseString = result.getText();
                            responseText.setText(responseString);
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        runOnUiThread(() -> {
                            // Show error message
                            responseText.setText("Sorry, I encountered an error: " + t.getMessage());
                            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                },
                Executors.newSingleThreadExecutor()
        );
    }
}