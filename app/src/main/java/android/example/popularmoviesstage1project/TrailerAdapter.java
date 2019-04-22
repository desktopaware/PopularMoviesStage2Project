package android.example.popularmoviesstage1project;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ItemViewHolder>{

    private List<String> trailers;
    Context context;
    private OnTrailerListener onTrailerListener;

    public TrailerAdapter(List<String> trailers, Context context, OnTrailerListener onTrailerListener) {
        this.trailers = trailers;
        this.context = context;
        this.onTrailerListener = onTrailerListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.traileritem, viewGroup, false);

        return new ItemViewHolder(view, onTrailerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        itemViewHolder.nubmerOfTrailers.setText("Trailer " + (i + 1));

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nubmerOfTrailers;
        OnTrailerListener onTrailerListener;

        public ItemViewHolder(@NonNull View itemView, OnTrailerListener onTrailerListener) {
            super(itemView);
            nubmerOfTrailers = itemView.findViewById(R.id.trailer_tv);
            this.onTrailerListener = onTrailerListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTrailerListener.onTrailerClick(getAdapterPosition());
        }
    }
    public interface OnTrailerListener{
        void onTrailerClick(int position);
    }
}
