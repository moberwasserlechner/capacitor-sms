declare global {
    interface PluginRegistry {
        Sms?: SmsPlugin;
    }
}

export interface SmsPlugin {
    /**
     * Send sms
     * @param {SmsOptions} options
     * @returns {Promise<any>}
     */
    send(options: SmsOptions): Promise<any>;

}

export interface SmsOptions {

}
