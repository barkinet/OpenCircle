package com.circleof6.view;

import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.model.Contact;
import com.circleof6.model.StatusUpdate;
import com.circleof6.util.MethodsUtils;

/**
 * Created by N-Pex on 2018-11-02.
 */
public class StatusViewHolder {

    public interface OnReplyListener {
        void onReply(Contact contact, View anchorButton);
    }

    private OnReplyListener onReplyListener;

    private ContactAvatarView avatarView;
    private View layoutEmoji;
    private TextView tvEmoji;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvStatus;
    private View layoutLocation;
    private TextView tvLocation;
    private View layoutAddResponse;
    private FloatingActionButton fabReply;

    public StatusViewHolder(View view) {
        avatarView = view.findViewById(R.id.avatarView);
        layoutEmoji = view.findViewById(R.id.avatarViewEmojiLayout);
        tvEmoji = view.findViewById(R.id.avatarViewEmoji);
        tvName = view.findViewById(R.id.tvContactName);
        tvDate = view.findViewById(R.id.tvDate);
        tvStatus = view.findViewById(R.id.tvStatus);
        layoutLocation = view.findViewById(R.id.locationLayout);
        tvLocation = view.findViewById(R.id.tvLocation);
        layoutAddResponse = view.findViewById(R.id.layoutAddResponse);
        fabReply = view.findViewById(R.id.fabReply);
    }

    public void populateWithContact(final Contact contact) {
        final StatusUpdate statusUpdate = CircleOf6Application.getInstance().getContactStatus(contact);

        avatarView.setContact(contact);
        if (statusUpdate != null && statusUpdate.getEmoji() != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(Character.toChars(statusUpdate.getEmoji()));
            tvEmoji.setText(sb);
        } else {
            layoutEmoji.setVisibility(View.GONE);
        }

        tvName.setText(contact.getName());

        if (statusUpdate != null) {
            tvDate.setText(MethodsUtils.dateDiffDisplayString(statusUpdate.getDate(), tvDate.getContext(), R.string.status_updated_ago_never, R.string.status_updated_ago_recently, R.string.status_updated_ago_minutes, R.string.status_updated_ago_minute, R.string.status_updated_ago_hours, R.string.status_updated_ago_hour, R.string.status_updated_ago_days, R.string.status_updated_ago_day));
            tvStatus.setText(statusUpdate.getMessage());
            if (TextUtils.isEmpty(statusUpdate.getLocation())) {
                // No location given
                layoutLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setText(statusUpdate.getLocation());
            }
            layoutAddResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnReplyListener() != null) {
                        getOnReplyListener().onReply(contact, layoutAddResponse);
                    }
                }
            });
            fabReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnReplyListener() != null) {
                        getOnReplyListener().onReply(contact, fabReply);
                    }
                }
            });
        } else {
            tvDate.setText(R.string.status_updated_ago_never);
            tvStatus.setVisibility(View.GONE);
            layoutLocation.setVisibility(View.GONE);
            layoutAddResponse.setVisibility(View.GONE);
            fabReply.setVisibility(View.GONE);
        }
    }

    public OnReplyListener getOnReplyListener() {
        return onReplyListener;
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }
}