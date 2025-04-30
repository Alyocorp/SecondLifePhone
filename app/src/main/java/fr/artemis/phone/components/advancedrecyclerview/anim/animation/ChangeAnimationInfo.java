package fr.artemis.phone.components.advancedrecyclerview.anim.animation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChangeAnimationInfo extends ItemAnimationInfo {

	private RecyclerView.ViewHolder newHolder, oldHolder;

	private int fromX, fromY, toX, toY;

	public ChangeAnimationInfo( RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
								int fromX, int fromY, int toX, int toY ) {
		this.oldHolder = oldHolder;
		this.newHolder = newHolder;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public RecyclerView.ViewHolder getNewHolder() {
		return newHolder;
	}

	public void setNewHolder( RecyclerView.ViewHolder newHolder ) {
		this.newHolder = newHolder;
	}

	public RecyclerView.ViewHolder getOldHolder() {
		return oldHolder;
	}

	public void setOldHolder( RecyclerView.ViewHolder oldHolder ) {
		this.oldHolder = oldHolder;
	}

	public int getFromX() {
		return fromX;
	}

	public void setFromX( int fromX ) {
		this.fromX = fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public void setFromY( int fromY ) {
		this.fromY = fromY;
	}

	public int getToX() {
		return toX;
	}

	public void setToX( int toX ) {
		this.toX = toX;
	}

	public int getToY() {
		return toY;
	}

	public void setToY( int toY ) {
		this.toY = toY;
	}

	@Override
	public RecyclerView.ViewHolder getAvailableViewHolder() {
		return ( oldHolder != null ) ? oldHolder : newHolder;
	}

	@Override
	public void clear( @NonNull RecyclerView.ViewHolder item ) {
		if ( oldHolder == item ) {
			oldHolder = null;
		}
		if ( newHolder == item ) {
			newHolder = null;
		}
		if ( oldHolder == null && newHolder == null ) {
			fromX = 0;
			fromY = 0;
			toX = 0;
			toY = 0;
		}
	}

	@NonNull
	@Override
	public String toString() {
		return "ChangeInfo{" +
				", oldHolder=" + oldHolder +
				", newHolder=" + newHolder +
				", fromX=" + fromX +
				", fromY=" + fromY +
				", toX=" + toX +
				", toY=" + toY +
				'}';
	}
}