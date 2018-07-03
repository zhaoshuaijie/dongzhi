package com.lcsd.dongzhi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lcsd.dongzhi.R;
import com.lcsd.dongzhi.entity.PaikeCommentlist;
import com.lcsd.dongzhi.util.StringUtil;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ================================================
 * 描    述：paike评论的Adapter
 * 修订历史：
 * ================================================
 */
public class CommentsAdapter extends BaseAdapter {
    private Context context;
    private List<PaikeCommentlist> evaluationReplies;

    public CommentsAdapter(Context context, @NonNull List<PaikeCommentlist> evaluationReplies) {
        this.context = context;
        this.evaluationReplies = evaluationReplies;
    }

    @Override
    public int getCount() {
        return evaluationReplies.size();
    }

    @Override
    public PaikeCommentlist getItem(int position) {
        return evaluationReplies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_comments, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        PaikeCommentlist replyItem = getItem(position);
        if (replyItem.getParent() != null) {
            SpannableString msp = new SpannableString((StringUtil.isEmpty(replyItem.getAlias()) ? "这家伙很懒" : replyItem.getAlias())
                    + "回复" + (StringUtil.isEmpty(replyItem.getParent().getUser()) ? "这家伙很懒" : replyItem.getParent().getUser())
                    + ":" + replyItem.getContent());
            msp.setSpan(new ForegroundColorSpan(0xFF1777CB), 0, replyItem.getAlias().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(0xFF1777CB), replyItem.getAlias().length() + 2, replyItem.getAlias().length() + replyItem.getParent().getUser().length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.reply.setText(msp);
        } else {
            SpannableString msp = new SpannableString((StringUtil.isEmpty(replyItem.getAlias()) ? "这家伙很懒" : replyItem.getAlias())
                    + ":" + replyItem.getContent());
            msp.setSpan(new ForegroundColorSpan(0xFF1777CB), 0, replyItem.getAlias().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.reply.setText(msp);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_reply)
        TextView reply;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}