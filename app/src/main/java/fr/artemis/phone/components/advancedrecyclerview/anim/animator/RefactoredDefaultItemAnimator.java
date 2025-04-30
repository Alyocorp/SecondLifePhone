package fr.artemis.phone.components.advancedrecyclerview.anim.animator;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.artemis.phone.components.advancedrecyclerview.anim.animation.AddAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animation.ChangeAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animation.MoveAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.animation.RemoveAnimationInfo;
import fr.artemis.phone.components.advancedrecyclerview.anim.manager.ItemAddAnimationManager;
import fr.artemis.phone.components.advancedrecyclerview.anim.manager.ItemChangeAnimationManager;
import fr.artemis.phone.components.advancedrecyclerview.anim.manager.ItemMoveAnimationManager;
import fr.artemis.phone.components.advancedrecyclerview.anim.manager.ItemRemoveAnimationManager;


public class RefactoredDefaultItemAnimator extends GeneralItemAnimator {

	@Override
	protected void onSetup() {
		setItemAddAnimationsManager( new DefaultItemAddAnimationManager( this ) );
		setItemRemoveAnimationManager( new DefaultItemRemoveAnimationManager( this ) );
		setItemChangeAnimationsManager( new DefaultItemChangeAnimationManager( this ) );
		setItemMoveAnimationsManager( new DefaultItemMoveAnimationManager( this ) );
	}

	@Override
	protected void onSchedulePendingAnimations() {
		schedulePendingAnimationsByDefaultRule();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If the payload list is not empty, RefactoredDefaultItemAnimator returns <code>true</code>.
	 * When this is the case:
	 * <ul>
	 * <li>If you override {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)}, both
	 * ViewHolder arguments will be the same instance.
	 * </li>
	 * <li>
	 * If you are not overriding {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)},
	 * then RefactoredDefaultItemAnimator will call {@link #animateMove(RecyclerView.ViewHolder, int, int, int, int)} and
	 * run a move animation instead.
	 * </li>
	 * </ul>
	 */
	@Override
	public boolean canReuseUpdatedViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads ) {
		return !payloads.isEmpty() || super.canReuseUpdatedViewHolder( viewHolder, payloads );
	}

	/**
	 * Item Animation manager for ADD operation  (Same behavior as DefaultItemAnimator class)
	 */
	protected static class DefaultItemAddAnimationManager extends ItemAddAnimationManager {

		public DefaultItemAddAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
			super( itemAnimator );
		}

		@Override
		protected void onCreateAnimation( @NonNull AddAnimationInfo info ) {
			if ( null != info.getAvailableViewHolder() ) {
				final ViewPropertyAnimatorCompat animator = ViewCompat.animate( info.getAvailableViewHolder().itemView );

				animator.alpha( 1 );
				animator.setDuration( getDuration() );

				startActiveItemAnimation( info, info.getAvailableViewHolder(), animator );
			}
		}

		@Override
		protected void onAnimationEndedSuccessfully( @NonNull AddAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		}

		@Override
		protected void onAnimationEndedBeforeStarted( @NonNull AddAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
			if ( null != item ) {
				item.itemView.setAlpha( 1 );
			}
		}

		@Override
		protected void onAnimationCancel( @NonNull AddAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
			item.itemView.setAlpha( 1 );
		}

		@Override
		public boolean addPendingAnimation( @NonNull RecyclerView.ViewHolder item ) {
			resetAnimation( item );

			item.itemView.setAlpha( 0 );

			enqueuePendingAnimationInfo( new AddAnimationInfo( item ) );

			return true;
		}
	}

	/**
	 * Item Animation manager for REMOVE operation  (Same behavior as DefaultItemAnimator class)
	 */
	protected static class DefaultItemRemoveAnimationManager extends ItemRemoveAnimationManager {

		public DefaultItemRemoveAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
			super( itemAnimator );
		}

		@Override
		protected void onCreateAnimation( @NonNull RemoveAnimationInfo info ) {
			if ( null != info.getAvailableViewHolder() ) {
				final ViewPropertyAnimatorCompat animator = ViewCompat.animate( info.getAvailableViewHolder().itemView );

				animator.setDuration( getDuration() );
				animator.alpha( 0 );

				startActiveItemAnimation( info, info.getAvailableViewHolder(), animator );
			}
		}

		@Override
		protected void onAnimationEndedSuccessfully( @NonNull RemoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
			final View view = item.itemView;
			view.setAlpha( 1 );
		}

		@Override
		protected void onAnimationEndedBeforeStarted( @NonNull RemoveAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
			if ( null != item ) {
				final View view = item.itemView;
				view.setAlpha( 1 );
			}
		}

		@Override
		protected void onAnimationCancel( @NonNull RemoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		}

		@Override
		public boolean addPendingAnimation( @NonNull RecyclerView.ViewHolder holder ) {
			resetAnimation( holder );

			enqueuePendingAnimationInfo( new RemoveAnimationInfo( holder ) );
			return true;
		}
	}

	/**
	 * Item Animation manager for CHANGE operation  (Same behavior as DefaultItemAnimator class)
	 */
	protected static class DefaultItemChangeAnimationManager extends ItemChangeAnimationManager {

		public DefaultItemChangeAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
			super( itemAnimator );
		}

		@Override
		protected void onCreateChangeAnimationForOldItem( @NonNull ChangeAnimationInfo info ) {
			final ViewPropertyAnimatorCompat animator = ViewCompat.animate( info.getOldHolder().itemView );

			animator.setDuration( getDuration() );
			animator.translationX( info.getToX() - info.getFromX() );
			animator.translationY( info.getToY() - info.getFromY() );
			animator.alpha( 0 );

			startActiveItemAnimation( info, info.getOldHolder(), animator );
		}

		@Override
		protected void onCreateChangeAnimationForNewItem( @NonNull ChangeAnimationInfo info ) {
			final ViewPropertyAnimatorCompat animator = ViewCompat.animate( info.getNewHolder().itemView );

			animator.translationX( 0 );
			animator.translationY( 0 );
			animator.setDuration( getDuration() );
			animator.alpha( 1 );

			startActiveItemAnimation( info, info.getNewHolder(), animator );
		}

		@Override
		protected void onAnimationEndedSuccessfully( @NonNull ChangeAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
			if ( null != item ) {
				final View view = item.itemView;
				view.setAlpha( 1 );
				view.setTranslationX( 0 );
				view.setTranslationY( 0 );
			}
		}

		@Override
		protected void onAnimationEndedBeforeStarted( @NonNull ChangeAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
			final View view = item.itemView;
			view.setAlpha( 1 );
			view.setTranslationX( 0 );
			view.setTranslationY( 0 );
		}

		@Override
		protected void onAnimationCancel( @NonNull ChangeAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		}

		@Override
		public boolean addPendingAnimation( @NonNull RecyclerView.ViewHolder oldHolder, @Nullable RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY ) {
			final float prevTranslationX = oldHolder.itemView.getTranslationX();
			final float prevTranslationY = oldHolder.itemView.getTranslationY();
			final float prevAlpha = oldHolder.itemView.getAlpha();

			resetAnimation( oldHolder );

			final int deltaX = (int) ( toX - fromX - prevTranslationX );
			final int deltaY = (int) ( toY - fromY - prevTranslationY );

			// recover prev translation state after ending animation
			oldHolder.itemView.setTranslationX( prevTranslationX );
			oldHolder.itemView.setTranslationY( prevTranslationY );
			oldHolder.itemView.setAlpha( prevAlpha );

			if ( newHolder != null ) {
				// carry over translation values
				resetAnimation( newHolder );
				newHolder.itemView.setTranslationX( -deltaX );
				newHolder.itemView.setTranslationY( -deltaY );
				newHolder.itemView.setAlpha( 0 );
			}

			enqueuePendingAnimationInfo( new ChangeAnimationInfo( oldHolder, newHolder, fromX, fromY, toX, toY ) );

			return true;
		}
	}

	/**
	 * Item Animation manager for MOVE operation  (Same behavior as DefaultItemAnimator class)
	 */
	protected static class DefaultItemMoveAnimationManager extends ItemMoveAnimationManager {

		public DefaultItemMoveAnimationManager( @NonNull BaseItemAnimator itemAnimator ) {
			super( itemAnimator );
		}

		@Override
		protected void onCreateAnimation( @NonNull MoveAnimationInfo info ) {
			if ( null != info.getAvailableViewHolder() ) {
				final View view = info.getAvailableViewHolder().itemView;
				final int deltaX = info.getToX() - info.getFromX();
				final int deltaY = info.getToY() - info.getFromY();

				if ( deltaX != 0 ) {
					ViewCompat.animate( view ).translationX( 0 );
				}
				if ( deltaY != 0 ) {
					ViewCompat.animate( view ).translationY( 0 );
				}

				final ViewPropertyAnimatorCompat animator = ViewCompat.animate( view );

				animator.setDuration( getDuration() );

				startActiveItemAnimation( info, info.getAvailableViewHolder(), animator );
			}
		}

		@Override
		protected void onAnimationEndedSuccessfully( @NonNull MoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
		}

		@Override
		protected void onAnimationEndedBeforeStarted( @NonNull MoveAnimationInfo info, @Nullable RecyclerView.ViewHolder item ) {
			if ( null != item ) {
				final View view = item.itemView;
				view.setTranslationY( 0 );
				view.setTranslationX( 0 );
			}
		}

		@Override
		protected void onAnimationCancel( @NonNull MoveAnimationInfo info, @NonNull RecyclerView.ViewHolder item ) {
			final View view = item.itemView;
			final int deltaX = info.getToX() - info.getFromX();
			final int deltaY = info.getToY() - info.getFromY();

			if ( deltaX != 0 ) {
				ViewCompat.animate( view ).translationX( 0 );
			}
			if ( deltaY != 0 ) {
				ViewCompat.animate( view ).translationY( 0 );
			}

			if ( deltaX != 0 ) {
				view.setTranslationX( 0 );
			}
			if ( deltaY != 0 ) {
				view.setTranslationY( 0 );
			}
		}

		@Override
		public boolean addPendingAnimation( @NonNull RecyclerView.ViewHolder item, int fromX, int fromY, int toX, int toY ) {
			final View view = item.itemView;

			fromX += item.itemView.getTranslationX();
			fromY += item.itemView.getTranslationY();

			resetAnimation( item );

			final int deltaX = toX - fromX;
			final int deltaY = toY - fromY;

			final MoveAnimationInfo info = new MoveAnimationInfo( item, fromX, fromY, toX, toY );

			if ( deltaX == 0 && deltaY == 0 && null != info.getAvailableViewHolder() ) {
				dispatchFinished( info, info.getAvailableViewHolder() );
				info.clear( info.getAvailableViewHolder() );
				return false;
			}

			if ( deltaX != 0 ) {
				view.setTranslationX( -deltaX );
			}
			if ( deltaY != 0 ) {
				view.setTranslationY( -deltaY );
			}

			enqueuePendingAnimationInfo( info );

			return true;
		}
	}
}