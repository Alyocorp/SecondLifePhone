package fr.artemis.phone.components.advancedrecyclerview.anim.animator;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;

import fr.artemis.phone.components.advancedrecyclerview.RecyclerViewSwipeManager;
import fr.artemis.phone.components.advancedrecyclerview.SwipeableItemViewHolder;
import fr.artemis.phone.components.advancedrecyclerview.anim.animation.RemoveAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.manager.ItemRemoveAnimationManager;


public class SwipeDismissItemAnimator extends DraggableItemAnimator {

	public static final Interpolator MOVE_INTERPOLATOR = new AccelerateDecelerateInterpolator();

	@Override
	protected void onSetup() {
		setItemAddAnimationsManager( new DefaultItemAddAnimationManager( this ) );
		setItemRemoveAnimationManager( new SwipeDismissItemRemoveAnimationManager( this ) );
		setItemChangeAnimationsManager( new DefaultItemChangeAnimationManager( this ) );
		setItemMoveAnimationsManager( new DefaultItemMoveAnimationManager( this ) );

		setRemoveDuration( 150 );
		setMoveDuration( 150 );
	}

	/**
	 * Item Animation manager for REMOVE operation  (Same behavior as DefaultItemAnimator class)
	 */
	protected static class SwipeDismissItemRemoveAnimationManager extends ItemRemoveAnimationManager {

		protected static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();

		public SwipeDismissItemRemoveAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
			super( itemAnimator );
		}

		protected static boolean isSwipeDismissed( RecyclerView.ViewHolder item ) {
			if ( !( item instanceof SwipeableItemViewHolder ) ) {
				return false;
			}

			final SwipeableItemViewHolder item2 = (SwipeableItemViewHolder) item;
			final int result = item2.getSwipeResult();
			final int reaction = item2.getAfterSwipeReaction();

			return ( ( result == RecyclerViewSwipeManager.RESULT_SWIPED_LEFT ) ||
					( result == RecyclerViewSwipeManager.RESULT_SWIPED_UP ) ||
					( result == RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT ) ||
					( result == RecyclerViewSwipeManager.RESULT_SWIPED_DOWN ) ) &&
					( reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM );
		}

		@Override
		protected void onCreateAnimation( @NonNull RemoveAnimationInfo info ) {
			final ViewPropertyAnimatorCompat animator;

			if ( isSwipeDismissed( info.getAvailableViewHolder() ) ) {
				final View view = info.getAvailableViewHolder().itemView;
				animator = ViewCompat.animate( view );
				animator.setDuration( getDuration() );
			} else {
				final View view = info.getAvailableViewHolder().itemView;
				animator = ViewCompat.animate( view );
				animator.setDuration( getDuration() );
				animator.setInterpolator( DEFAULT_INTERPOLATOR );
				animator.alpha( 0 );
			}

			startActiveItemAnimation( info, info.getAvailableViewHolder(), animator );
		}

		protected static boolean isSwipeDismissed( RemoveAnimationInfo info ) {
			return ( info instanceof SwipeDismissRemoveAnimationInfo );
		}

		@Override
		protected void onAnimationEndedSuccessfully( @NonNull RemoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
			final View view = item.itemView;

			if ( isSwipeDismissed( info ) ) {
				view.setTranslationX( 0 );
				view.setTranslationY( 0 );
			} else {
				view.setAlpha( 1 );
			}
		}

		@Override
		protected void onAnimationEndedBeforeStarted( @NonNull RemoveAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
			if ( null != item ) {
				final View view = item.itemView;

				if ( isSwipeDismissed( info ) ) {
					view.setTranslationX( 0 );
					view.setTranslationY( 0 );
				} else {
					view.setAlpha( 1 );
				}
			}
		}

		@Override
		protected void onAnimationCancel( @NonNull RemoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		}

		@Override
		public boolean addPendingAnimation( @NonNull RecyclerView.ViewHolder holder ) {
			if ( isSwipeDismissed( holder ) ) {
				final View itemView = holder.itemView;
				final int prevItemX = (int) ( itemView.getTranslationX() + 0.5f );
				final int prevItemY = (int) ( itemView.getTranslationY() + 0.5f );

				endAnimation( holder );

				itemView.setTranslationX( prevItemX );
				itemView.setTranslationY( prevItemY );

				enqueuePendingAnimationInfo( new SwipeDismissRemoveAnimationInfo( holder ) );

				return true;
			} else {
				endAnimation( holder );

				enqueuePendingAnimationInfo( new RemoveAnimationInfo( holder ) );

				return true;
			}
		}
	}

	protected static class SwipeDismissRemoveAnimationInfo extends RemoveAnimationInfo {

		public SwipeDismissRemoveAnimationInfo( @NonNull RecyclerView.ViewHolder holder ) {
			super( holder );
		}
	}
}