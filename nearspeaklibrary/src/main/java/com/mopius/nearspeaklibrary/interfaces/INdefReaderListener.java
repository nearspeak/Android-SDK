package com.mopius.nearspeaklibrary.interfaces;

/**
 * @author Appaya
 *
 */
public interface INdefReaderListener {
    /**
     * this is a callback method wich gets called when a NFC tag was read successfully
     * @param _message
     */
	void onNdefGotRead(final String _message);
}