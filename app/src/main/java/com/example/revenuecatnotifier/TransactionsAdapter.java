package com.example.revenuecatnotifier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private Transaction[] transactions;

    public TransactionsAdapter(Transaction[] transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // putting attache to root = true, will not work.
        // you will always write this line of code when you want to specify the layout you want the recycler view to render.
        // in a simpler way, it links the ViewHolder to the actual item layout we created.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        // here we are going to set the values that we need to the item layout.
        holder.bind(transactions[position], position);
    }

    // this is the first method that is going to be called.
    @Override
    public int getItemCount() {
        return transactions.length;
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

//        private TextView titleTextView;
//        private TextView descriptionTextView;
//        private ImageView projectImage;
//        private CardView projectCard;
//        private final int DEFAULT_MARGIN = 16;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            // linking the xml elements to a variables.
            // you will need to call the findViewById from the itemView variable.
//            projectImage = itemView.findViewById(R.id.image_portfolio);
//            titleTextView = itemView.findViewById(R.id.text_view_portfolio_title);
//            descriptionTextView = itemView.findViewById(R.id.text_view_portfolio_description);
//            projectCard = itemView.findViewById(R.id.card_project);
        }

        // this functions is called bind by convention.
        public void bind(Transaction transaction, int position) {
            // here we are going to set all the texts and images and variables inside of our item layout
//            titleTextView.setText(project.getTitle());
//            descriptionTextView.setText(project.getDescription());
//            projectImage.setImageResource(project.getImage());
//
//            ViewGroup.MarginLayoutParams layoutParams =
//                    (ViewGroup.MarginLayoutParams) projectCard.getLayoutParams();
//
//            if (position == 0) {
//                layoutParams.setMargins(DEFAULT_MARGIN, DEFAULT_MARGIN * 2, DEFAULT_MARGIN, DEFAULT_MARGIN);
//            } else {
//                layoutParams.setMargins(DEFAULT_MARGIN, 0, DEFAULT_MARGIN, DEFAULT_MARGIN);
//            }
//            projectCard.requestLayout();
        }
    }
}
