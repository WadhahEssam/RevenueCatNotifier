package com.example.revenuecatnotifier;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private Transaction[] transactions;

    public TransactionsAdapter(Transaction[] transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(transactions[position], position, transactions.length);
    }

    @Override
    public int getItemCount() {
        return transactions.length;
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTextView;
        private ImageView transactionIconImageView;
        private TextView uidTextView;
        private TextView skuTextView;
        private TextView transactionAmountTextView;
        private CardView transactionCard;

        private final int DEFAULT_HOR_MARGIN = 50;
        private final int DEFAULT_VER_MARGIN = 16;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            registerElements();
        }

        public void bind(Transaction transaction, int position, int transactionsLength) {
            statusTextView.setText(getTransactionStatus(transaction));
            transactionAmountTextView.setText(getTransactionAmount(transaction));
            uidTextView.setText(transaction.getUid());
            skuTextView.setText(transaction.getSku().substring(0, transaction.getSku().length() > 30 ? 30 : transaction.getSku().length()));
            transactionIconImageView.setImageResource(getTransactionIconResource(transaction));

            colorize(getTransactionColor(transaction));

            // fixing margins
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) transactionCard.getLayoutParams();
            if (position == transactionsLength - 1) {
                layoutParams.setMargins(DEFAULT_HOR_MARGIN, DEFAULT_VER_MARGIN, DEFAULT_HOR_MARGIN, 100);
            } else {
                layoutParams.setMargins(DEFAULT_HOR_MARGIN, DEFAULT_VER_MARGIN, DEFAULT_HOR_MARGIN, DEFAULT_VER_MARGIN);
            }
            transactionCard.requestLayout();
        }

        private void registerElements() {
            statusTextView = itemView.findViewById(R.id.text_view_transaction_status);
            transactionIconImageView = itemView.findViewById(R.id.image_view_transaction_icon);
            uidTextView = itemView.findViewById(R.id.text_view_transaction_uid_value);
            skuTextView = itemView.findViewById(R.id.text_view_transaction_sku_value);
            transactionAmountTextView = itemView.findViewById(R.id.text_view_transaction_amount);
            transactionCard = itemView.findViewById(R.id.card_transaction);
        }

        private String getTransactionColor(Transaction transaction) {
            if (transaction.getRevenue().equals("Trial")) {
                return "#759EC4";
            }
            if (transaction.getRevenue().equals("$0.00")) {
                return "#759EC4";
            }
            if (transaction.getIsRenewal() == true) {
                return "#45D5C1";
            }
            return "#FFCA3C";
        }

        private void colorize(String color) {
            statusTextView.setTextColor(Color.parseColor(color));
            transactionAmountTextView.setTextColor(Color.parseColor(color));
        }

        private int getTransactionIconResource(Transaction transaction) {
            if (transaction.getRevenue().equals("Trial")) {
                return R.drawable.free_2;
            }
            if (transaction.getRevenue().equals("$0.00")) {
                return R.drawable.free_2;
            }
            if (transaction.getIsRenewal() == true) {
                return R.drawable.refresh_3;
            }
            return R.drawable.dollars;
        }

        private String getTransactionAmount(Transaction transaction) {
            if (transaction.getRevenue().equals("Trial")) {
                return "$0.00";
            }
            return transaction.getRevenue();
        }

        private String getTransactionStatus(Transaction transaction) {
            if (transaction.getRevenue().equals("Trial")) {

                return "Trial";
            }
            if (transaction.getRevenue().equals("$0.00")) {
                return "Grace Period";
            }
            if (transaction.getIsRenewal() == true) {
                return "Renewal";
            }
            return "Active";
        }
    }
}
