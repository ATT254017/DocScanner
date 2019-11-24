package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class DocumentAdapter extends ListAdapter<Document, DocumentAdapter.DocumentHolder> {
    private OnItemClickListener listener;

    public DocumentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Document> DIFF_CALLBACK = new DiffUtil.ItemCallback<Document>() {
        @Override
        public boolean areItemsTheSame(@NonNull Document oldItem, @NonNull Document newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Document oldItem, @NonNull Document newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getContent().equals(newItem.getContent());
        }
    };

    @NonNull
    @Override
    public DocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.document_item, parent, false);
        return new DocumentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentHolder holder, int position) {
        Document currentDocument = getItem(position);
        holder.textViewTitle.setText(currentDocument.getTitle());
        holder.textViewContent.setText(currentDocument.getContent());
    }

    public Document getDocumentAt(int position) {
        return getItem(position);
    }

    class DocumentHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewContent;


        public DocumentHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewContent = itemView.findViewById(R.id.tv_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Document document);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
