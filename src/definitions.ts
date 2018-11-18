declare global {
    interface PluginRegistry {
        SmsManager?: SmsManagerPlugin;
    }
}

export interface SmsManagerPlugin {
    /**
     * Send single sms
     * @param {SmsSendOptions} options
     * @returns {Promise<void>}
     */
    send(options: SmsSendOptions): Promise<void>;

}

export interface SmsSendOptions {
    numbers: string[];
    text: string;
    android?: {
        /**
         *
         */
        subscriptionId?: string;
        /**
         * Use this if your app is not allowed to send sms using the SmsManager because Google limited your permissions.
         * https://android-developers.googleblog.com/2018/10/providing-safe-and-secure-experience.html
         * If true a SMS app will be opened for each message you send to the plugin.
         */
        openSmsApp?: boolean;
    }
}
