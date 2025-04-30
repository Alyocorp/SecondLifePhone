package fr.artemis.phone.fragments.utilisateur.documents.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.dto.CRMPhoneGEDDocumentDTO;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

	private List<CRMPhoneGEDDocumentDTO> mData;
	private LayoutInflater mInflater;
	private ItemClickListener mClickListener;

	// data is passed into the constructor
	public DocumentAdapter( Context context, List<CRMPhoneGEDDocumentDTO> data ) {
		this.mInflater = LayoutInflater.from( context );
		this.mData = data;
	}

	// inflates the row layout from xml when needed
	@Override
	public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
		View view = mInflater.inflate( R.layout.fragment_documents_line, parent, false );
		return new ViewHolder( view );
	}

	// binds the data to the TextView in each row
	@Override
	public void onBindViewHolder( ViewHolder holder, int position ) {
		String document = mData.get( position ).getNom();
		holder.tvDocumentLine.setText( document );
	}

	// total number of rows
	@Override
	public int getItemCount() {
		return mData.size();
	}

	// convenience method for getting data at click position
	public CRMPhoneGEDDocumentDTO getItem(int id) {
		return mData.get(id);
	}

	// stores and recycles views as they are scrolled off screen
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView( R.id.tvDocumentLine )
		TextView tvDocumentLine;

		ViewHolder( View itemView ) {
			super( itemView );

			ButterKnife.bind( this, itemView );
			itemView.setOnClickListener( this );
		}

		@Override
		public void onClick( View view ) {
			if ( mClickListener != null )
				mClickListener.onItemClick( view, getAdapterPosition() );
		}
	}

	// allows clicks events to be caught
	public void setClickListener( ItemClickListener itemClickListener ) {
		this.mClickListener = itemClickListener;
	}

	// parent activity will implement this method to respond to click events
	public interface ItemClickListener {

		void onItemClick( View view, int position );
	}
}