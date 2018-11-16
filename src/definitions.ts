declare global {
    interface PluginRegistry {
        SmsManager?: SmsManagerPlugin;
    }
}

export interface SmsManagerPlugin {
    /**
     * Send sms
     * @param {SmsSendOptions} options
     * @returns {Promise<any>}
     */
    send(options: SmsSendOptions): Promise<any>;

}

export interface SmsSendOptions {

}
