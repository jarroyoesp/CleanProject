package es.jarroyo.cleanproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import es.jarroyo.cleanproject.R;
import es.jarroyo.cleanproject.base.UseCaseHandler;
import es.jarroyo.cleanproject.base.ui.BaseNavigationActivity;
import es.jarroyo.cleanproject.contract.DataPresenter;
import es.jarroyo.cleanproject.data.source.DataRepository;
import es.jarroyo.cleanproject.data.source.remote.RemoteDataSource;
import es.jarroyo.cleanproject.domain.usecase.GetDataUseCase;
import es.jarroyo.cleanproject.fragments.MainFragment;
import es.jarroyo.cleanproject.fragments.Section2Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends BaseNavigationActivity {

    MainFragment fragmentSection1;
    Fragment fragmentSection2;

    //Presenter
    DataPresenter mDataPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar(getString(R.string.main_fragment_title));

        checkLocationPermissions();
        addFirstFragment();
        createActivitiesPresenter(fragmentSection1);
    }

    private void addFirstFragment() {
        fragmentSection1 = MainFragment.newInstance();
        addFragmentToMainContent(fragmentSection1, MainFragment.TAG);
    }

    @Override
    public boolean showMenuRightToolbar() {
        return false;
    }

    @Override
    public void onClickNavigationMenuItem(int position) {
        hideDrawerLayout();
        switch (position) {
            case 0:
                if (!fragmentSection1.isAdded()) {
                    replaceFragmentToMainContent(fragmentSection1, MainFragment.TAG);
                }

                break;
            case 1:
                if (fragmentSection2 == null) {
                    fragmentSection2 = Section2Fragment.newInstance();
                }

                if (!fragmentSection2.isAdded()) {
                    replaceFragmentToMainContent(fragmentSection2, Section2Fragment.TAG);
                }

                break;
        }
    }

    /**
     * CREATE PRESENTER
     *
     * @param mainFragment
     */
    private void createActivitiesPresenter(MainFragment mainFragment){
        mDataPresenter = new DataPresenter(UseCaseHandler.getInstance(),
                mainFragment,
                new GetDataUseCase(provideDataRepository(this))
        );
    }

    /**
     * Lo Correcto sería usar Dagger para inytectar las dependecias y así poder usar un repositorio
     * mockeado para los test. Para este ejemplo no usamos Dagger.
     * @param context
     * @return
     */
    public static DataRepository provideDataRepository(@NonNull Context context) {
        checkNotNull(context);
        return DataRepository.getInstance(RemoteDataSource.getInstance(context));
    }

    private void checkLocationPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    11);
        }
    }

}
