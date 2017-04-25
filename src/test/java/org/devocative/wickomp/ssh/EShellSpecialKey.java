package org.devocative.wickomp.ssh;

import java.util.HashMap;
import java.util.Map;

/**
 * https://unix.stackexchange.com/questions/116562/key-bindings-table
 */
public enum EShellSpecialKey {
	BACKSPACE(8, new byte[]{(byte) 0x7f}, "BACKSPACE"),
	TAB(9, new byte[]{(byte) 0x09}, "TAB"),
	CTR(17, new byte[]{}, "CTR"),
	ENTER(13, new byte[]{(byte) 0x0d}, "ENTER"),
	ESC(27, new byte[]{(byte) 0x1b}, "ESC"),

	PAGE_UP(33, "\033[5~".getBytes(), "PAGE_UP"),
	PAGE_DOWN(34, "\033[6~".getBytes(), "PAGE_DOWN"),

	END(35, "\033[4~".getBytes(), "END"),
	HOME(36, "\033[1~".getBytes(), "HOME"),

	LEFT(37, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x44}, "LEFT"),
	UP(38, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x41}, "UP"),
	RIGHT(39, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x43}, "RIGHT"),
	DOWN(40, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x42}, "DOWN"),

	INSERT(45, "\033[2~".getBytes(), "INSERT"),
	DELETE(46, "\033[3~".getBytes(), "DELETE"),

	CTR_A(65, new byte[]{(byte) 0x01}, "CTR_A"),
	CTR_B(66, new byte[]{(byte) 0x02}, "CTR_B"),
	CTR_C(67, new byte[]{(byte) 0x03}, "CTR_C"),
	CTR_D(68, new byte[]{(byte) 0x04}, "CTR_D"),
	CTR_E(69, new byte[]{(byte) 0x05}, "CTR_E"),
	CTR_F(70, new byte[]{(byte) 0x06}, "CTR_F"),
	CTR_G(71, new byte[]{(byte) 0x07}, "CTR_G"),
	CTR_H(72, new byte[]{(byte) 0x08}, "CTR_H"),
	CTR_I(73, new byte[]{(byte) 0x09}, "CTR_I"),
	CTR_J(74, new byte[]{(byte) 0x0A}, "CTR_J"),
	CTR_K(75, new byte[]{(byte) 0x0B}, "CTR_K"),
	CTR_L(76, new byte[]{(byte) 0x0C}, "CTR_L"),
	CTR_M(77, new byte[]{(byte) 0x0D}, "CTR_M"),
	CTR_N(78, new byte[]{(byte) 0x0E}, "CTR_N"),
	CTR_O(79, new byte[]{(byte) 0x0F}, "CTR_O"),
	CTR_P(80, new byte[]{(byte) 0x10}, "CTR_P"),
	CTR_Q(81, new byte[]{(byte) 0x11}, "CTR_Q"),
	CTR_R(82, new byte[]{(byte) 0x12}, "CTR_R"),
	CTR_S(83, new byte[]{(byte) 0x13}, "CTR_S"),
	CTR_T(84, new byte[]{(byte) 0x14}, "CTR_T"),
	CTR_U(85, new byte[]{(byte) 0x15}, "CTR_U"),
	CTR_V(86, new byte[]{(byte) 0x16}, "CTR_V"),
	CTR_W(87, new byte[]{(byte) 0x17}, "CTR_W"),
	CTR_X(88, new byte[]{(byte) 0x18}, "CTR_X"),
	CTR_Y(89, new byte[]{(byte) 0x19}, "CTR_Y"),
	CTR_Z(90, new byte[]{(byte) 0x1A}, "CTR_Z"),

	F1(112, "\033[11~".getBytes(), "F1"),
	F2(113, "\033[12~".getBytes(), "F2"),
	F3(114, "\033[13~".getBytes(), "F3"),
	F4(115, "\033[14~".getBytes(), "F4"),
	F5(116, "\033[15~".getBytes(), "F5"),
	//y ?(   , "\033[16~".getBytes(), "??"),
	F6(117, "\033[17~".getBytes(), "F6"),
	F7(118, "\033[18~".getBytes(), "F7"),
	F8(119, "\033[19~".getBytes(), "F8"),
	F9(120, "\033[20~".getBytes(), "F9"),
	F10(121, "\033[21~".getBytes(), "F10"),
	F11(122, "\033[23~".getBytes(), "F11"),
	F12(123, "\033[24~".getBytes(), "F12"),

	CTR_BRACKET_OPEN(219, new byte[]{(byte) 0x1B}, "CTR_["),
	CTR_BRACKET_CLOSE(221, new byte[]{(byte) 0x1D}, "CTR_]"),

	NONE(0, null, null);

	private static final Map<Integer, EShellSpecialKey> KEY_MAP = new HashMap<>();

	// ------------------------------

	private final int keyCode;
	private final byte[] shellCode;
	private final String keyName;

	// ------------------------------

	EShellSpecialKey(int keyCode, byte[] shellCode, String keyName) {
		this.keyCode = keyCode;
		this.shellCode = shellCode;
		this.keyName = keyName;
	}

	// ------------------------------

	public int getKeyCode() {
		return keyCode;
	}

	public byte[] getShellCode() {
		return shellCode;
	}

	public String getKeyName() {
		return keyName;
	}

	// ---------------

	@Override
	public String toString() {
		return keyName;
	}

	// ------------------------------

	public static EShellSpecialKey findShellSpecialKey(int keyCode) {
		init();
		if (KEY_MAP.containsKey(keyCode)) {
			return KEY_MAP.get(keyCode);
		}
		return NONE;
	}

	public static byte[] findShellCode(int keyCode) {
		init();

		if (KEY_MAP.containsKey(keyCode)) {
			return KEY_MAP.get(keyCode).getShellCode();
		}

		return null;
	}

	// ------------------------------

	private static void init() {
		if (KEY_MAP.isEmpty()) {
			for (EShellSpecialKey specialKey : values()) {
				KEY_MAP.put(specialKey.keyCode, specialKey);
			}
		}
	}
}
