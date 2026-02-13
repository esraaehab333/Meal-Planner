package com.example.mealplanner.presentation.account.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.presentation.account.presenter.AccountPresenter;
import com.example.mealplanner.presentation.account.presenter.AccountPresenterImp;
import com.example.mealplanner.utils.CustomDialog;
import com.example.mealplanner.utils.CustomSnackbar;

public class AccountFragment extends Fragment implements AccountView {

    private static final String TAG = "AccountFragment";
    private AccountPresenter presenter;
    private AppCompatButton btnLogout;
    private TextView tvUserName, tvUserEmail;
    AppCompatButton btnSync ;
    private SharedPreferanceLocalDataSource sharedPref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSync=view.findViewById(R.id.btnSync);
        sharedPref = new SharedPreferanceLocalDataSource(requireContext());
        presenter = new AccountPresenterImp(this, requireContext());
        String name = sharedPref.getUserName();
        String email = sharedPref.getUserEmail();
        android.util.Log.d("DEBUG_DATA", "Name: " + name + " Email: " + email);
        tvUserName.setText(name);
        tvUserEmail.setText(email);

        if ("GUEST".equals(sharedPref.getUserId())) {
            btnLogout.setText("Exit Guest Mode");
        }
        btnSync.setOnClickListener(v -> {
            presenter.onSyncClicked();
        });
        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }
    private void showLogoutConfirmationDialog() {
        String currentUserId = sharedPref.getUserId();
        boolean isGuest = "GUEST".equals(currentUserId);

        String title = isGuest ? "Exit Guest Mode" : "Logout";
        String message = isGuest ? "Exit now to create an account and save your meals?"
                : "Are you sure you want to log out?";

        CustomDialog dialog = CustomDialog.newInstance(
                R.drawable.ic_logout,
                title,
                message,
                isGuest ? "Exit" : "Logout",
                "Cancel",
                (dialogInterface, which) -> presenter.onLogoutClicked(),
                (dialogInterface, which) -> dialogInterface.dismiss()
        );
        dialog.show(getParentFragmentManager(), "LogoutDialog");
    }

    @Override
    public void navigateToLogin() {
        if (getView() != null) {
            NavController navController = Navigation.findNavController(requireView());
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(navController.getGraph().getStartDestinationId(), true)
                    .build();
            navController.navigate(R.id.loginFregment, null, navOptions);
        }
    }

    @Override
    public void showLogoutError(String message) {
        CustomSnackbar.showError(getView(),message);
    }
    @Override
    public void showSyncSuccess() {
        CustomSnackbar.showError(getView(),"Data synced successfully to cloud!");
    }

    @Override
    public void showSyncError(String error) {
        CustomSnackbar.showError(getView(),"Sync Failed");
    }
}