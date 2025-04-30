package fr.artemis.phone.components.advancedrecyclerview.base.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.ExpandableItemState;
import fr.artemis.phone.components.advancedrecyclerview.ExpandableItemStateFlags;


public abstract class AbstractExpandableItemViewHolder extends RecyclerView.ViewHolder implements ExpandableItemViewHolder {

	private final ExpandableItemState mExpandState = new ExpandableItemState();

	public AbstractExpandableItemViewHolder( @NonNull View itemView ) {
		super( itemView );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExpandStateFlags( @ExpandableItemStateFlags int flags ) {
		mExpandState.setFlags( flags );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@ExpandableItemStateFlags
	public int getExpandStateFlags() {
		return mExpandState.getFlags();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@NonNull
	public ExpandableItemState getExpandState() {
		return mExpandState;
	}
}