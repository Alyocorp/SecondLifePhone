package fr.artemis.phone.components.advancedrecyclerview.anim.animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AddAnimationInfo extends ItemAnimationInfo {

	private RecyclerView.ViewHolder holder;

	public AddAnimationInfo( @NonNull RecyclerView.ViewHolder holder ) {
		this.holder = holder;
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
		return "AddAnimationInfo{" +
				"holder=" + holder +
				'}';
	}
}