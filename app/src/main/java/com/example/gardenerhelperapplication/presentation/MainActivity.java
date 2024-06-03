package com.example.gardenerhelperapplication.presentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gardenerhelperapplication.R;
import com.example.gardenerhelperapplication.databinding.ActivityMainBinding;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.MyPlantsCatalog;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist.PlantsInGroundList;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist.PlantsOnSeedlingsList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.add(binding.fragmentContainer.getId(), MyPlantsCatalog.class, null);
            transaction.commit();

            binding.navView.setCheckedItem(R.id.navMyPlantsCatalog);
        }

        binding.navView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if(binding.navView.getCheckedItem()!= null) {
                if(binding.navView.getCheckedItem().getItemId() == id) { // Если пользователь уже находится в одном из каталогов ратсений, то не нужно запускать фрагмент с ним заново
                    closeDrawer();
                    return true;
                }
            }

            if (id == R.id.navMyPlantsCatalog) {
                replaceFragment(binding.fragmentContainer.getId(), MyPlantsCatalog.class);
                closeDrawer();
                return true;
            }
            if (id == R.id.navPlantsOnSeedlingsCatalog) {
                closeDrawer();
                replaceFragment(binding.fragmentContainer.getId(), PlantsOnSeedlingsList.class);
                return true;
            }
            if (id == R.id.navPlantsInGroundCatalog) {
                closeDrawer();
                replaceFragment(binding.fragmentContainer.getId(), PlantsInGroundList.class);
                return true;
            }
            return false;
        });

    }

    /**
     * Заменяет фрагменты в соответствии с выбором каталога пользователем в меню
     */
    private <T extends Fragment> void replaceFragment(int containerId, Class<T> fragmentClass) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(containerId, fragmentClass, null);
        transaction.commit();
    }

    /**
     * Блокирует открывание NavigationDrawer по свайпу
     */
    public void lockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * Разблокирует открывание NavigationDrawer по свайпу
     */
    public void unlockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * Открывает NavigationDrawer
     */
    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * Закрывает NavigationDrawer
     */
    public void closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Устанавливает в меню с навигацией, что сейчас пользователь находится в личном каталоге растений
     * (при перехода из каталога растений на рассаде или высаженных в грунт в личный каталог растений)
     */
    public void setNavigationMenuItem() {
        binding.navView.setCheckedItem(R.id.navMyPlantsCatalog);
    }
}