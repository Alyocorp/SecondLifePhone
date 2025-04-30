package fr.artemis.phone.components.advancedrecyclerview.anim.manager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.anim.animation.RemoveAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.BaseItemAnimator;

public abstract class ItemRemoveAnimationManager extends BaseItemAnimationManager<RemoveAnimationInfo> {

	private static final String TAG = "ARVItemRemoveAnimMgr";

	public ItemRemoveAnimationManager( BaseItemAnimator itemAnimator ) {
		super( itemAnimator );
	}

	@Override
	public long getDuration() {
		return mItemAnimator.getRemoveDuration();
	}

	@Override
	public void setDuration( long duration ) {
		mItemAnimator.setRemoveDuration( duration );
	}

	@Override
	public void dispatchStarting( @NonNull RemoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchRemoveStarting(" + item + ")" );
		}
		mItemAnimator.dispatchRemoveStarting( item );
	}

	@Override
	public void dispatchFinished( @NonNull RemoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchRemoveFinished(" + item + ")" );
		}
		mItemAnimator.dispatchRemoveFinished( item );
	}

	@Override
	protected boolean endNotStartedAnimation( @NonNull RemoveAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
		if ( ( info.getAvailableViewHolder() != null ) && ( ( item == null ) || ( info.getAvailableViewHolder() == item ) ) ) {
			onAnimationEndedBeforeStarted( info, info.getAvailableViewHolder() );
			dispatchFinished( info, info.getAvailableViewHolder() );
			info.clear( info.getAvailableViewHolder() );
			return true;
		} else {
			return false;
		}
	}

	public abstract boolean addPendingAnimation( RecyclerView.ViewHolder holder );
}