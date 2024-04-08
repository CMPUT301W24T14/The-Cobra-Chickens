package com.example.eventplanner;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventplanner.databinding.FragmentPushNotificationsBinding;

public class PushnotificationFragment extends Fragment {

    private FragmentPushNotificationsBinding binding;
    private PushnotificationViewModel pushnotificationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPushNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // ViewModel
        pushnotificationViewModel = new ViewModelProvider(this).get(PushnotificationViewModel.class);

        // Check permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Handle permission
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        onClickListeners();

        return view;
    }

    private void onClickListeners() {
        binding.buttonSend.setOnClickListener(v -> sendNewMessage());
        binding.buttonChangeTopic.setOnClickListener(v -> subscribeToNewTopic());
    }

    private void sendNewMessage() {
        String title = binding.titleEdittext.getText().toString();
        String message = binding.messageEdittext.getText().toString();
        String topic = binding.recipientTopicEdittext.getText().toString();

        if (!title.isEmpty() && !message.isEmpty() && !topic.isEmpty()) {
            PushNotification pushNotification = new PushNotification(
                    new NotificationData(title, message),
                    "/topics/" + topic
            );
            pushnotificationViewModel.sendNewMessage(pushNotification);
        } else {
            Toast.makeText(requireContext(), "MISSING INFORMATION", Toast.LENGTH_SHORT).show();
        }
    }

    private void subscribeToNewTopic() {
        String topicInput = binding.newTopicEdittext.getText().toString();
        String formattedTopicName = topicInput.replaceAll("\\s+", "_");
        if (!formattedTopicName.isEmpty()) {
            pushnotificationViewModel.subscribeToNewTopic(formattedTopicName, new TopicCallback() {
                @Override
                public void onSubscribed() {
                    // Setting the Text View
                    binding.textviewCurrentTopic.setText(formattedTopicName);
                    // Saving Topic in SharedPref
                    SharedPreferencesHelper.setCurrentTopic(requireContext(), formattedTopicName);
                }
            });
        } else {
            Toast.makeText(requireContext(), "NO TOPIC PROVIDED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
