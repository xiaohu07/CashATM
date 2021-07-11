public enum Denomination {
    TWENTY(20),
    FIFTY(50);

    private final int noteValue;

    Denomination(int noteValue) {
        this.noteValue = noteValue;
    }

    public int getNoteValue() {
        return noteValue;
    }
}
