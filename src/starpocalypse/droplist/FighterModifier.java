package starpocalypse.droplist;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FighterModifier implements DropListModifier {

    private final List<String> fighterList;

    @Override
    public void modify() {}
}