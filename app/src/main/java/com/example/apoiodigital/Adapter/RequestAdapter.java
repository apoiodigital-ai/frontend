package com.example.apoiodigital.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Utils.FontUtils;
import com.example.apoiodigital.Utils.RequestItem;
import com.example.apoiodigital.View.HistoryResponseActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RequestItem> requisicoes;

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_ITEM = 1;

    public RequestAdapter(List<RequestItem> requisicoes) {
        this.requisicoes = requisicoes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_TITLE){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_title, parent, false);

            return new TitleViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_button, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RequestItem requestItem = requisicoes.get(position);

        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).textView.setText(requestItem.title);
        } else if (holder instanceof ButtonViewHolder) {
            ((ButtonViewHolder) holder).btn.setText(requestItem.requisicao.getPrompt());
            ((ButtonViewHolder) holder).btn.setTag(requestItem.requisicao.getId());


            ((ButtonViewHolder) holder).btn.setOnClickListener(l -> {
                String titulo = ((ButtonViewHolder) holder).btn.getText().toString();
                String tag = ((ButtonViewHolder) holder).btn.getTag().toString();

                Intent intent = new Intent(holder.itemView.getContext(), HistoryResponseActivity.class);
                intent.putExtra("button_id", tag);
                intent.putExtra("title_req", titulo);

                holder.itemView.getContext().startActivity(intent);


            });
        }

        applyFont(holder.itemView);
    }

    @Override
    public int getItemViewType(int position) {
        RequestItem requestItem = requisicoes.get(position);

        return requestItem.type == RequestItem.Type.TITLE? TYPE_TITLE: TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return requisicoes.size();
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public TitleViewHolder(@NonNull View view){
            super(view);
            textView = view.findViewById(R.id.dataRequestTitle);
        }
    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder{
        MaterialButton btn;

        public ButtonViewHolder(@NonNull View view){
            super(view);
            btn = view.findViewById(R.id.requestItemBtn);
        }
    }

    private void applyFont(View root) {
        if (root instanceof TextView) {
            FontUtils.applyFontSize(root.getContext(), (TextView) root);
        } else if (root instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                applyFont(vg.getChildAt(i));
            }
        }
    }
}
