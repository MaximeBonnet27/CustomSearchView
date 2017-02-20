package com.maxmax.customsearchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CustomSearchView extends LinearLayout {

    private static final String TAG = CustomSearchView.class.getSimpleName();
    private LinearLayout mLinearLayout;
    private EditText mEditText;
    private ImageButton mSearchButton;
    private ImageButton mClearButton;
    private RecyclerView mRecyclerView;
    private CustomSearchAdapter mCustomSearchAdapter;

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSearchView, 0, 0);
        initializeLayout(typedArray);
        initializeListeners();
    }

    private void initializeLayout(TypedArray typedArray) {
        View view = inflate(getContext(), R.layout.custom_search_view, this);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.root_layout);
        mEditText = (EditText) view.findViewById(R.id.edit_text);
        mSearchButton = (ImageButton) view.findViewById(R.id.search_image_button);
        mSearchButton.setImageDrawable(typedArray.getDrawable(R.styleable.CustomSearchView_searchDrawable));
        mClearButton = (ImageButton) view.findViewById(R.id.clear_image_button);
        mClearButton.setImageDrawable(typedArray.getDrawable(R.styleable.CustomSearchView_clearDrawable));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mCustomSearchAdapter = new CustomSearchAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mCustomSearchAdapter);
    }


    private void initializeListeners() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditText.isFocused()) {
                    setClearIconVisible(charSequence.length() > 0);
                }
                getResults(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                if (hasFocus) {
                    setClearIconVisible(mEditText.getText().length() > 0);
                } else {
                    setClearIconVisible(false);
                }
            }
        });
        mSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditText.requestFocus()) {
                    showKeyboard();
                }
            }
        });
        mClearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
                hideKeyboard(view);
            }
        });
        mCustomSearchAdapter.addListener(new CustomSearchAdapterListener() {
            @Override
            public void onSizeChanged(int size) {
                if (size == 0) {
                    mLinearLayout.setBackgroundResource(R.drawable.round_corners_top_only);
                } else {
                    mLinearLayout.setBackgroundResource(R.drawable.round_corners);
                }
            }
        });
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getResults(String input) {
        mCustomSearchAdapter.generateData(input);
    }

    private void setClearIconVisible(boolean showClear) {
        if (showClear) {
            mClearButton.setVisibility(VISIBLE);
            mSearchButton.setVisibility(GONE);
        } else {
            mClearButton.setVisibility(GONE);
            mSearchButton.setVisibility(VISIBLE);
        }
    }
}
