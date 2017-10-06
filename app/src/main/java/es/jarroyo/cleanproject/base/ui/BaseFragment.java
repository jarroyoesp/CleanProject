package es.jarroyo.cleanproject.base.ui;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BaseFragment extends Fragment {

  private Unbinder unbinder;

  protected View inflateView(LayoutInflater inflater, ViewGroup container, int layoutResId) {
    View view = inflater.inflate(layoutResId, container, false);
    bindView(view);
    return view;
  }

  @Override
  public void onDestroyView() {
    if (unbinder != null) {
      unbinder.unbind();

      unbinder = null;
    }

    super.onDestroyView();
  }

  private void bindView(View view) {
    if(unbinder != null)
      unbinder.unbind();

    unbinder = ButterKnife.bind(this, view);
  }
}
