package fr.artemis.phone.components.advancedrecyclerview.anim.animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MoveAnimationInfo extends ItemAnimationInfo {

	private final int fromX;
	private final int fromY;
	private final int toX;
	private final int toY;
	private RecyclerView.ViewHolder holder;

	public MoveAnimationInfo( @NonNull RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY ) {
		this.holder = holder;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getToX() {
		return toX;
	}

	public int getToY() {
		return toY;
	}

	@Override
	@Nullable
	public RecyclerView.ViewHolder getAvailableViewHolder() {
		return holder;
	}

	@Override
	public void clear( @NonNull RecyclerView.ViewHolder item ) {
		if ( holder == item ) {
			holder = null;
		}
	}

	@NonNull
	@Override
	public String toString() {
		return "MoveAnimationInfo{" +
				"holder=" + holder +
				", fromX=" + fromX +
				", fromY=" + fromY +
				", toX=" + toX +
				", toY=" + toY +
				'}';
	}
}