package giampy.simon.customcalculator.themes;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import giampy.simon.customcalculator.Boast;
import giampy.simon.customcalculator.Calculator;
import giampy.simon.customcalculator.Constants;
import giampy.simon.customcalculator.MainActivity;
import giampy.simon.customcalculator.R;
import giampy.simon.customcalculator.RandomNumber;

public class AppleFragment extends Fragment {

    private TextView edit;
    private TextView solution;

    public static boolean sqrt;
    public static float textSize;
    public static int DRG = 0;
    public Typeface roboto;
    Constants constants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getContext().setTheme(R.style.AppleTheme);
        View view = inflater.inflate(R.layout.fragment_apple, container, false);

        roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        Bundle bundle = this.getArguments();
        sqrt = bundle.getBoolean(MainActivity.SQRT, false);
        boolean enableThousands = bundle.getBoolean(MainActivity.ENABLETHOUSANDS, true);
        String thousandsStyle = "";
        if (enableThousands) {
            thousandsStyle = bundle.getString(MainActivity.THOUSANDSSTYLE);
        }
        textSize = bundle.getFloat(MainActivity.SIZE, 35);

        constants = new Constants(enableThousands, thousandsStyle);
        new Calculator(constants, DRG);

        initializations(view);

        final float size = Math.round(textSize / 100 * 70);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            edit.post(new Runnable() {
                @Override
                public void run() {
                    if (edit.getLineCount() > 1) {
                        edit.setTextSize(size);
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EDIT", edit.getText().toString());
        outState.putString("SOLU", solution.getText().toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState!=null) {
            edit.setText(savedInstanceState.getString("EDIT"));
            solution.setText(savedInstanceState.getString("SOLU"));
        }
    }

    public boolean isTheOrientationLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void write_numbers(View view) {// scrive un numero premuto
        //caso particolare, quando è stato già mostrato il risultato
        if (!solution.getText().toString().equals("")) {
            edit.setText("");
            if (!isTheOrientationLandscape()) {
                edit.setTextSize(textSize);
            }
            solution.setText("");
        }
        String txt = edit.getText().toString();

        Button button = (Button) view;
        String number_pressed = button.getText().toString();
        String output = Calculator.write_numbers(txt, number_pressed);

        edit.setText(output);
    }

    public void write_comma() { //scrive la virgola
        //caso particolare in cui è già stato mostrato il risultato
        if (!solution.getText().toString().equals("")) {
            solution.setText("");
            edit.setText("0");
            if (!isTheOrientationLandscape()) {
                edit.setTextSize(textSize);
            }
        }

        String txt = edit.getText().toString();

        String output = Calculator.write_comma(txt);
        edit.setText(output);
    }

    public void write_ac() { //azzera l' input e l' output
        edit.setText("");
        solution.setText("");
        if (!isTheOrientationLandscape()){
            edit.setTextSize(textSize);
        }
    }

    public void write_back() { //cancella il numero precedente
        String txt = edit.getText().toString();
        if (!solution.getText().toString().equals("")) {
            solution.setText("");
        } else {
            if (txt.length()!=0) {
                if (txt.length() >= 2) {
                    String confronto = txt.substring(txt.length() - 2, txt.length());
                    if (confronto.equals("√(")|| confronto.equals("n(")|| confronto.equals("g(") || confronto.equals("s(") || confronto.equals("h(") || confronto.equals("m(") || confronto.equals(", ")) {
                        switch (confronto) {
                            case "√(":
                            case ", ": txt = txt.substring(0, txt.length() - 1); break;
                            case "h(": txt = txt.substring(0, txt.length() - 4); break;
                            case "m(": txt = txt.substring(0, txt.length() - 6); break;
                            case "g(":
                            case "s(": txt = txt.substring(0, txt.length() - 3); break;
                            default:
                                if (txt.substring(txt.length() - 3, txt.length()).equals("ln(")) {
                                    txt = txt.substring(0, txt.length() - 2);
                                } else {
                                    txt = txt.substring(0, txt.length() - 3);
                                }
                                break;
                        }
                    }
                }
                String output = txt.substring(0, txt.length()-1);
                //formatta numero
                if (output.length()> 3) {
                    if (output.charAt(output.length()-1) == '0' || output.charAt(output.length()-1) == '1' || output.charAt(output.length()-1) == '2' || output.charAt(output.length()-1) == '3' || output.charAt(output.length()-1) == '4' || output.charAt(output.length()-1) == '5' ||
                            output.charAt(output.length()-1) == '6' || output.charAt(output.length()-1) == '7' || output.charAt(output.length()-1) == '8' || output.charAt(output.length()-1) == '9') {
                        output = Calculator.format(output);
                    }
                }
                if (output==null) {
                    output = txt.substring(0, txt.length()-1);
                }
                edit.setText(output);
            }
        }
    }

    public void write_symbol(View view) { //scrive il simbolo
        reWrite();
        String txt = edit.getText().toString();

        Button button = (Button) view;
        String symbol_pressed = button.getText().toString();
        String output;
        if (symbol_pressed.equals("√")) {
            output = Calculator.write_root(txt, "√");
        } else {
            output = Calculator.write_symbol(txt, symbol_pressed);
        }
        edit.setText(output);
    }

    public void write_minus() { //scrive il segno -
        reWrite();
        String txt = edit.getText().toString();

        String output = Calculator.write_minus(txt);
        edit.setText(output);
    }

    public void write_parenthesis() { //scrive parentesi
        reWrite();
        String txt = edit.getText().toString();

        String output = Calculator.write_parenthesis(txt);
        edit.setText(output);
    }

    public void write_power(View view) {
        reWrite();
        String txt = edit.getText().toString();

        Button button = (Button) view;
        String power_pressed = null;
        switch (button.getId()) {
            case R.id.apple_squared: power_pressed="2"; break;
            case R.id.apple_cubed: power_pressed="3"; break;
            case R.id.apple_power: power_pressed="x"; break;
            case R.id.apple_raised_to_minus1: power_pressed="−1"; break;
        }
        String output = Calculator.write_power(txt, power_pressed);
        edit.setText(output);
    }

    public void write_factorial() {
        reWrite();
        String txt = edit.getText().toString();

        String output = Calculator.write_fact(txt);
        edit.setText(output);
    }

    public void write_root(View view) {
        reWrite();
        String txt = edit.getText().toString();
        String root_pressed = "√";
        if (view.getId() == R.id.apple_cubed_root) {
            root_pressed = "∛";
        }
        String output = Calculator.write_root(txt, root_pressed);
        edit.setText(Html.fromHtml(output));
    }

    public void write_SCTHLOGSPIE(View view) {
        reWrite();
        String txt = edit.getText().toString();

        Button button = (Button) view;
        String scth = button.getText().toString();
        String output = Calculator.write_SCTHLOGSPIE(txt, scth);
        edit.setText(output);
    }

    public void write_exp() {
        reWrite();
        String txt = edit.getText().toString();
        String output = Calculator.write_exp(txt);
        edit.setText(output);
    }

    public void write_random() {
        startActivityForResult(new Intent(getContext(), RandomNumber.class), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String min = "0";
        String max = "0";
        if (resultCode == 1) {
            min = data.getStringExtra("MIN");
            max = data.getStringExtra("MAX");
        }
        try {
            if (Long.parseLong(min, 10) > Long.parseLong(max, 10)) {
                String temp = max;
                max = min;
                min = temp;
            }
        } catch (NumberFormatException ex ){
            min = String.valueOf(Long.MAX_VALUE);
            max = String.valueOf(Long.MAX_VALUE);
            Boast.makeText(getContext(), R.string.too_long).show();
        } finally {
            if (Long.parseLong(min, 10) > Long.parseLong(max, 10)) {
                String temp = max;
                max = min;
                min = temp;
            }
        }

        String output = "random(" + min + ", " + max + ")";
        String txt = edit.getText().toString();
        output = Calculator.write_SCTHLOGSPIE(txt, output);
        edit.setText(output);
    }

    public void write_ans() {
        reWrite();
        String lastAnswer = "";
        try {
            File dir = new File(getContext().getCacheDir().getAbsolutePath());
            File answer = new File(dir, "answers.txt");
            FileInputStream fis = new FileInputStream(answer);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

            lastAnswer = bufferedReader.readLine();
            bufferedReader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String txt = edit.getText().toString();
        String output=txt;
        if (lastAnswer.equals("Error")) {
            lastAnswer = "0";
        } else if (!lastAnswer.equals("")) {
            lastAnswer = lastAnswer.substring(1);
        }
        if (txt.length()==0) {
            output = Calculator.write_numbers(txt, lastAnswer);
        } else if (txt.charAt(txt.length() - 1) == '%' || txt.charAt(txt.length() - 1) == '+' || txt.charAt(txt.length() - 1) == '×' || txt.charAt(txt.length() - 1) == '÷' || txt.charAt(txt.length() - 1) == '−' || txt.charAt(txt.length() - 1) == '(' ||
                txt.charAt(txt.length() - 1) == '^') {
            output = Calculator.write_numbers(txt, lastAnswer);
        }
        edit.setText(output);
    }

    public void write_equal() { //risolve il calcolo dato
        String txt = edit.getText().toString();

        String output = Calculator.check(txt);
        if (output.equals("null"))  {
            output = "";
            solution.setText(output);
        }
        edit.setText(output);
        txt = output;

        output = Calculator.equal(txt);
        if (output.equals("Azzera")) {
            edit.setText("0");
            output = "=0";
        } else if (output.contains("toast")) {
            output = output.replace("toast", "");
            Boast.makeText(getContext(), R.string.fact_too_big).show();
        }
        solution.setText(output);

        if (output.equals("rror"))  {
            output = "0";
        }

        try {
            File dir = new File(getContext().getCacheDir().getAbsolutePath());
            File answer = new File(dir, "answers.txt");
            FileOutputStream fos = new FileOutputStream(answer);
            fos.write(output.getBytes());
            fos.close();
        } catch (IOException ex) {
            Log.d("tag", ex.toString());
        }
    }

    public void reWrite() { //chiamato quando è stata già mostrato la soluzione e bisogna modificare il testo input
        if (!solution.getText().toString().equals("")) {
            String newText = solution.getText().toString().substring(1, solution.getText().toString().length());
            if (newText.equals("rror")) {
                newText = "0";
                edit.setText(newText);
            } else {
                if (newText.contains("-")) {
                    newText = newText.replace("-", "−");
                }
                edit.setText(newText);
            }
            solution.setText("");
            if (!isTheOrientationLandscape()) {
                edit.setTextSize(textSize);
            }
        }
    }

    public void initializations(View view) {
        Button n0 = (Button) view.findViewById(R.id.apple_n0);
        Button n1 = (Button) view.findViewById(R.id.apple_n1);
        Button n2 = (Button) view.findViewById(R.id.apple_n2);
        Button n3 = (Button) view.findViewById(R.id.apple_n3);
        Button n4 = (Button) view.findViewById(R.id.apple_n4);
        Button n5 = (Button) view.findViewById(R.id.apple_n5);
        Button n6 = (Button) view.findViewById(R.id.apple_n6);
        Button n7 = (Button) view.findViewById(R.id.apple_n7);
        Button n8 = (Button) view.findViewById(R.id.apple_n8);
        Button n9 = (Button) view.findViewById(R.id.apple_n9);
        Button ac = (Button) view.findViewById(R.id.apple_ac);
        Button comma = (Button) view.findViewById(R.id.apple_comma);
        Button extra = (Button) view.findViewById(R.id.apple_extra);
        Button division = (Button) view.findViewById(R.id.apple_division);
        ImageButton backspace = (ImageButton) view.findViewById(R.id.apple_backspace);
        Button multiplier = (Button) view.findViewById(R.id.apple_multiplier);
        Button plus = (Button) view.findViewById(R.id.apple_plus);
        Button minus = (Button) view.findViewById(R.id.apple_minus);
        Button equal = (Button) view.findViewById(R.id.apple_equal);
        Button parenthesis = (Button) view.findViewById(R.id.apple_parenthesis);
        Button squared = (Button) view.findViewById(R.id.apple_squared);
        Button cubed = (Button) view.findViewById(R.id.apple_cubed);
        Button power = (Button) view.findViewById(R.id.apple_power);
        Button raised_to_minus1 = (Button) view.findViewById(R.id.apple_raised_to_minus1);
        Button root = (Button) view.findViewById(R.id.apple_root);
        Button cubed_root = (Button) view.findViewById(R.id.apple_cubed_root);
        Button random = (Button) view.findViewById(R.id.apple_random);
        Button factorial = (Button) view.findViewById(R.id.apple_factorial);
        Button sin = (Button) view.findViewById(R.id.apple_sin);
        Button cos = (Button) view.findViewById(R.id.apple_cos);
        Button tan = (Button) view.findViewById(R.id.apple_tan);
        Button e = (Button) view.findViewById(R.id.apple_e);
        Button sinh = (Button) view.findViewById(R.id.apple_sinh);
        Button cosh = (Button) view.findViewById(R.id.apple_cosh);
        Button tanh = (Button) view.findViewById(R.id.apple_tanh);
        Button log = (Button) view.findViewById(R.id.apple_log);
        Button pi = (Button) view.findViewById(R.id.apple_pi);
        Button ln = (Button) view.findViewById(R.id.apple_ln);
        Button exp = (Button) view.findViewById(R.id.apple_exp);
        Button ans = (Button) view.findViewById(R.id.apple_ans);
        edit = (TextView) view.findViewById(R.id.apple_edit);
        solution = (TextView) view.findViewById(R.id.apple_solution);

        edit.setTypeface(roboto);
        solution.setTypeface(roboto);
        n0.setTypeface(roboto);
        n1.setTypeface(roboto);
        n2.setTypeface(roboto);
        n3.setTypeface(roboto);
        n4.setTypeface(roboto);
        n5.setTypeface(roboto);
        n6.setTypeface(roboto);
        n7.setTypeface(roboto);
        n8.setTypeface(roboto);
        n9.setTypeface(roboto);
        ac.setTypeface(roboto);
        comma.setTypeface(roboto);
        extra.setTypeface(roboto);
        division.setTypeface(roboto);
        multiplier.setTypeface(roboto);
        minus.setTypeface(roboto);
        equal.setTypeface(roboto);
        parenthesis.setTypeface(roboto);
        plus.setTypeface(roboto);

        if (isTheOrientationLandscape()) {
            squared.setTypeface(roboto);
            cubed.setTypeface(roboto);
            power.setTypeface(roboto);
            raised_to_minus1.setTypeface(roboto);
            root.setTypeface(roboto);
            cubed_root.setTypeface(roboto);
            random.setTypeface(roboto);
            factorial.setTypeface(roboto);
            sin.setTypeface(roboto);
            cos.setTypeface(roboto);
            tan.setTypeface(roboto);
            exp.setTypeface(roboto);
            sinh.setTypeface(roboto);
            cosh.setTypeface(roboto);
            tanh.setTypeface(roboto);
            log.setTypeface(roboto);
            pi.setTypeface(roboto);
            e.setTypeface(roboto);
            ans.setTypeface(roboto);
            ln.setTypeface(roboto);

            squared.setText(Html.fromHtml(squared.getText().toString()));
            cubed.setText(Html.fromHtml(cubed.getText().toString()));
            power.setText(Html.fromHtml(power.getText().toString()));
            raised_to_minus1.setText(Html.fromHtml(raised_to_minus1.getText().toString()));
            cubed_root.setText(Html.fromHtml(cubed_root.getText().toString()));
            random.setText(Html.fromHtml(random.getText().toString()));

            View.OnClickListener powListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_power(v);
                }
            };
            squared.setOnClickListener(powListener);
            cubed.setOnClickListener(powListener);
            power.setOnClickListener(powListener);
            raised_to_minus1.setOnClickListener(powListener);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_root(v);
                }
            });
            cubed_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_root(v);
                }
            });
            random.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_random();
                }
            });
            View.OnClickListener scthListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {write_SCTHLOGSPIE(v);
                }
            };
            sin.setOnClickListener(scthListener);
            cos.setOnClickListener(scthListener);
            tan.setOnClickListener(scthListener);
            sinh.setOnClickListener(scthListener);
            cosh.setOnClickListener(scthListener);
            tanh.setOnClickListener(scthListener);
            log.setOnClickListener(scthListener);
            ln.setOnClickListener(scthListener);
            pi.setOnClickListener(scthListener);
            e.setOnClickListener(scthListener);
            exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_exp();
                }
            });
            factorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_factorial();
                }
            });
            ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    write_ans();
                }
            });
        }

        if (sqrt && !isTheOrientationLandscape()) {
            extra.setText(R.string.root);
        }
        comma.setText(constants.getPoint());

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) { //controlla se sono state scritte troppe cifre e ridimensiona il testo, oppure impedisce che si continui a scrivere
                int count = edit.getLineCount();
                if (!isTheOrientationLandscape()) {
                    if (count > 2) {
                        Boast.makeText(getContext(), R.string.digits_limit, Toast.LENGTH_LONG).show();
                        edit.setText(Calculator.format(edit.getText().toString().substring(0, edit.getText().length() - 1)));
                    }
                    if (count > 1) {
                        float size = Math.round(textSize / 100 * 70);
                        edit.setTextSize(size);
                    }
                } else {
                    if (count > 1) {
                        Boast.makeText(getContext(), R.string.digits_limit, Toast.LENGTH_LONG).show();
                        edit.setText(Calculator.format(edit.getText().toString().substring(0, edit.getText().length() - 1)));
                    }
                }
            }
        });

        if (isTheOrientationLandscape()) {
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.apple_radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.apple_deg:
                            DRG = 0;
                            break;
                        case R.id.apple_rad:
                            DRG = 1;
                            break;
                        case R.id.apple_grad:
                            DRG = 2;
                            break;
                    }
                    Calculator.setDRG(DRG);
                }
            });
        }

        View.OnClickListener numListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_numbers(v);
            }
        };
        n0.setOnClickListener(numListener);
        n1.setOnClickListener(numListener);
        n2.setOnClickListener(numListener);
        n3.setOnClickListener(numListener);
        n4.setOnClickListener(numListener);
        n5.setOnClickListener(numListener);
        n6.setOnClickListener(numListener);
        n7.setOnClickListener(numListener);
        n8.setOnClickListener(numListener);
        n9.setOnClickListener(numListener);
        comma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_comma();
            }
        });
        parenthesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_parenthesis();
            }
        });
        View.OnClickListener symListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_symbol(v);
            }
        };
        plus.setOnClickListener(symListener);
        multiplier.setOnClickListener(symListener);
        division.setOnClickListener(symListener);
        extra.setOnClickListener(symListener);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_minus();
            }
        });
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_equal();
            }
        });
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_ac();
            }
        });
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_back();
            }
        });
        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                write_ac();
                return true;
            }
        });
    }
}

