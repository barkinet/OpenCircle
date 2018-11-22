package com.circleof6.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.circleof6.R;
import com.circleof6.model.ContactStatusReply;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class RepliesViewPagerAdapter extends PagerAdapter {

    private static final int ID_EMPTY = 0;
    private static final int ID_CALL = 1;
    private static final int ID_MESSAGE = 2;
    private static final int ID_WHATSAPP = 3;

    private Context context;
    private List<ContactStatusReply> replies;

    private Map<Integer, List<ContactStatusReply>> categorizedReplies;
    private final Integer[] categorizedReplyKeys;

    public RepliesViewPagerAdapter(Context context, TabLayout tabLayout, List<ContactStatusReply> replies) {
        this.context = context;
        this.replies = replies;
        categorizedReplies = new HashMap<>();
        for (ContactStatusReply reply : replies) {
            int key = 0;
            switch (reply.getType()) {
                case Call:
                    key = ID_CALL;
                    break;
                case Message:
                    key = ID_MESSAGE;
                    break;
                case WhatsApp:
                    key = ID_WHATSAPP;
                    break;
                case Emoji:
                    key = reply.getEmoji();
                    break;
            }
            if (key > 0) {
                if (!categorizedReplies.containsKey(key)) {
                    categorizedReplies.put(key, new ArrayList<ContactStatusReply>());
                }
                categorizedReplies.get(key).add(reply);
            }
        }
        if (categorizedReplies.size() == 0) {
            // Nothing, add a default item, need this for view pager to react to nested scrolls!
            categorizedReplies.put(ID_EMPTY, null);
        }
        categorizedReplyKeys = categorizedReplies.keySet().toArray(new Integer[0]);

        // Sort
        Arrays.sort(categorizedReplyKeys);
        tabLayout.removeAllTabs();
        for (int i = 0; i < categorizedReplyKeys.length; i++) {
            int key = categorizedReplyKeys[i];
            if (key == ID_EMPTY) {
                continue;
            }
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(R.layout.status_reply_tab_item);
            tab.setText(String.format("%d", categorizedReplies.get(key).size()));
            View icon = tab.getCustomView().findViewById(android.R.id.icon);
            TextView textualIcon = tab.getCustomView().findViewById(R.id.textual_icon);
            switch (key) {
                case ID_CALL:
                    tab.setIcon(R.drawable.ic_reply_call);
                    textualIcon.setVisibility(View.GONE);
                    break;
                case ID_MESSAGE:
                    tab.setIcon(R.drawable.ic_reply_message);
                    textualIcon.setVisibility(View.GONE);
                    break;
                case ID_WHATSAPP:
                    tab.setIcon(R.drawable.ic_reply_whatsapp);
                    textualIcon.setVisibility(View.GONE);
                    break;
                default:
                    icon.setVisibility(View.GONE);
                    StringBuffer sb = new StringBuffer();
                    sb.append(Character.toChars(key));
                    textualIcon.setText(sb);
                    textualIcon.setVisibility(View.VISIBLE);
                    break;
            }
            tabLayout.addTab(tab);
        }
    }

    @Override
    public int getCount() {
        return this.categorizedReplies.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        int key = categorizedReplyKeys[position];

        if (key == ID_EMPTY) {
            // Create an empty view, just so that the viewpager will react to nested scrolls in the content area.
            View view = new View(container.getContext());
            view.setBackgroundColor(Color.TRANSPARENT);
            container.addView(view);
            return view;
        }

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.status_reply_page, container, false);
        RecyclerView rvReplies = view.findViewById(R.id.rvReplies);
        rvReplies.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));
        rvReplies.setAdapter(new StatusReplyPageRecyclerViewAdapter(container.getContext(), categorizedReplies.get(key)));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (View) object == view;
    }

/*    @Override
    public CharSequence getPageTitle(int position) {
        int key = categorizedReplyKeys[position];
        List<StatusUpdateReply> replies = categorizedReplies.get(key);

        switch (key) {
            case ID_CALL:
                return buildStringWithDrawable(R.drawable.ic_reply_call, String.format("%d", replies.size()));
            case ID_MESSAGE:
                return buildStringWithDrawable(R.drawable.ic_reply_message, String.format("%d", replies.size()));
            case ID_WHATSAPP:
                return buildStringWithDrawable(R.drawable.ic_reply_whatsapp, String.format("%d", replies.size()));
            default:
                StringBuffer sb = new StringBuffer();
                sb.append(Character.toChars(replies.get(0).getEmoji()));
                sb.append(String.format(" %d", replies.size()));
                return sb;
        }
    }*/

/*    private CharSequence buildStringWithDrawable(int drawable, String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder("   " + text); // space added before text for convenience
        try {
            Drawable d = ContextCompat.getDrawable(context, drawable);
            d.setBounds(5, 5, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sb;
    }*/
}
