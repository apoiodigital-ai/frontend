package com.example.apoiodigital.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Dto.IAResponseDTO;
import com.example.apoiodigital.Utils.FontUtils;

import java.util.List;

public class HistoryResponseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<IAResponseDTO> iaResponseDTOList;
    private String title;

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_MESSAGE = 1;

    public HistoryResponseAdapter(List<IAResponseDTO> iaResponseDTOList, Context context, String title) {
        this.iaResponseDTOList = iaResponseDTOList;
        this.context = context;
        this.title = title;
    }

    @Override
    public int getItemCount() {
        //Adiconei mais um por causa do titulo
        return iaResponseDTOList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TITLE : TYPE_MESSAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_TITLE) {
            View view = inflater.inflate(R.layout.title_history, parent, false);
            return new TitleViewHolder(view);
        }

        View view = inflater.inflate(R.layout.item_response, parent, false);
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).title.setText(title);

        } else if (holder instanceof MessageViewHolder) {
            // Ajuste por causa do titulo
            IAResponseDTO iaResponseDTO = iaResponseDTOList.get(position - 1);
            ((MessageViewHolder) holder).subTitle.setText(position + "º Passo");
            ((MessageViewHolder) holder).message.setText(iaResponseDTO.getiaMessage());
        }

        applyFont(holder.itemView);
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TitleViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.itemResponseTitleHistory);
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView subTitle;
        TextView message;

        public MessageViewHolder(@NonNull View view) {
            super(view);
            subTitle = view.findViewById(R.id.itemResponseSubTitle);
            message = view.findViewById(R.id.itemResponseMessage);
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
