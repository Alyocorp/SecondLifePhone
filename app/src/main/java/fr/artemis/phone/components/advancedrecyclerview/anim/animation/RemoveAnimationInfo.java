package fr.artemis.phone.components.advancedrecyclerview.anim.animation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemoveAnimationInfo extends ItemAnimationInfo {

	private RecyclerView.ViewHolder holder;

	public RemoveAnimationInfo( RecyclerView.ViewHolder holder ) {
		this.holder = holder;
	}

	@Override
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
		return "RemoveAnimationInfo{" +
				"holder=" + holder +
				'}';
	}
}