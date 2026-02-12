package com.example.mealplanner.presentation.account.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.presentation.account.presenter.AccountPresenter;
import com.example.mealplanner.presentation.account.presenter.AccountPresenterImp;
import com.example.mealplanner.utils.CustomDialog;

public class AccountFragment extends Fragment implements AccountView {

    private AccountPresenter presenter;
    private Button btnLogout;
    private SharedPreferanceLocalDataSource sharedPref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref = new SharedPreferanceLocalDataSource(requireContext());
        presenter = new AccountPresenterImp(this, requireContext());
        btnLogout = view.findViewById(R.id.btnLogout);
        if ("GUEST".equals(sharedPref.getUserId())) {
            btnLogout.setText("Exit Guest Mode");
        }

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
                (dialogInterface, which) -> {
                    presenter.onLogoutClicked();
                },
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}