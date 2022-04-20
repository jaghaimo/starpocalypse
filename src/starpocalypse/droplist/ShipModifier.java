package starpocalypse.droplist;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShipModifier implements DropListModifier {

    private final List<String> weaponList;

    @Override
    public void modify() {}
}
