package com.googlecode.lanterna.issue;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Issue155 {
    public static void main(String... args) throws IOException {
        Terminal term = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(term);
        WindowManager windowManager = new DefaultWindowManager();
        Component background = new EmptySpace(TextColor.ANSI.DEFAULT);
        final WindowBasedTextGUI gui = new MultiWindowTextGUI(screen, windowManager, background);
        screen.startScreen();
        gui.addWindowAndWait(new BasicWindow("Issue155") {{
            setComponent(createUi(gui, this));
        }});
        screen.stopScreen();
    }


    private static Panel createUi(WindowBasedTextGUI gui, BasicWindow window) {
        return createUi(gui, window, 1);
    }

    private static Panel createUi(WindowBasedTextGUI gui, final BasicWindow window, final int counter) {
        final int nextCounter = counter + 3;
        return Panels.vertical(
                new Button("Open Dialog (and crush stuff)", openDialog(gui, window, nextCounter)),
                new CheckBoxList<String>() {{
                    for (int i = counter; i < nextCounter; ++i) {
                        addItem(String.valueOf(i));
                    }
                }},
                new Button("Quit", new Runnable() {
                    @Override public void run() { window.close(); }
                })
        );
    }

    private static Runnable openDialog(final WindowBasedTextGUI gui, final BasicWindow window, final int counter) {
        return new Runnable() {
            @Override public void run() {
                new ActionListDialogBuilder().
                        setCanCancel(true).
                        addAction("Reinstall UI (this crashes everything)", setupUI(gui, window, counter)).
                        build().
                        showDialog(gui);
            }
        };
    }

    private static Runnable setupUI(final WindowBasedTextGUI gui, final BasicWindow window, final int counter) {
        return new Runnable() {
            @Override public void run() {
                window.setComponent(createUi(gui, window, counter));
            }
        };
    }
}
