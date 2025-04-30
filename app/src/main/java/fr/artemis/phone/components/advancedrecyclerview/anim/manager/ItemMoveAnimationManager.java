package fr.artemis.phone.components.advancedrecyclerview.anim.manager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.anim.animation.MoveAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animator.BaseItemAnimator;

public abstract class ItemMoveAnimationManager extends BaseItemAnimationManager<MoveAnimationInfo> {

	public static final String TAG = "ARVItemMoveAnimMgr";

	public ItemMoveAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
		super( itemAnimator );
	}

	@Override
	public long getDuration() {
		return mItemAnimator.getMoveDuration();
	}

	@Override
	public void setDuration( long duration ) {
		mItemAnimator.setMoveDuration( duration );
	}

	@Override
	public void dispatchStarting( @NonNull MoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchMoveStarting(" + item + ")" );
		}
		mItemAnimator.dispatchMoveStarting( item );
	}

	@Override
	public void dispatchFinished( @NonNull MoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		if ( debugLogEnabled() ) {
			Log.d( TAG, "dispatchMoveFinished(" + item + ")" );
		}
		mItemAnimator.dispatchMoveFinished( item );
	}

	@Override
	protected boolean endNotStartedAnimation( @NonNull MoveAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
		if ( ( info.getAvailableViewHolder() != null ) && ( ( item == null ) || ( info.getAvailableViewHolder() == item ) ) ) {
			onAnimationEndedBeforeStarted( info, info.getAvailableViewHolder() );
			dispatchFinished( info, info.getAvailableViewHolder() );
			info.clear( info.getAvailableViewHolder() );
			return true;
		} else {
			return false;
		}
	}

	public abstract boolean addPendingAnimation( @NonNull RecyclerView.ViewHolder item, int fromX, int fromY, int toX, int toY );
}