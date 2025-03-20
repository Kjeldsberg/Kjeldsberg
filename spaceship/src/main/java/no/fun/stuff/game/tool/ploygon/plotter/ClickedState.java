package no.fun.stuff.game.tool.ploygon.plotter;

public class ClickedState {
    private CState state = CState.START;
    public ClickedState() {

    }

    public CState getState() {
        return state;
    }

    public void leftClick() {
        if(state == CState.START) {
            state = CState.FIRST;
        }else
        if(state == CState.FIRST) {
            state = CState.MAKE;
        }else
        if(state == CState.MAKE) {
            state = CState.MAKE;
        }
    }
    public void reset() {
        if(state == CState.MAKE) {
            state = CState.FIRST;
        }else {
            state = CState.START;
        }
    }
}
