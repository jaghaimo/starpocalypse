package starpocalypse.droplist;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeaponModifier implements DropListModifier {

    private final List<String> weaponList;

    @Override
    public void modify() {}
}
