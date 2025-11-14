export const overlayVariants = { hidden: { opacity: 0 }, visible: { opacity: 1 }, exit: { opacity: 0 } };
export const modalVariants = {
    hidden: { y: "100vh" },
    visible: { y: "0", transition: { type: "spring", stiffness: 70, damping: 15 } },
    exit: { y: "100vh", opacity: 0, transition: { duration: 0.4 } }
};
export const newMessageVariants = {
    hidden: { opacity: 0, y: 10, scale: 0.95 },
    visible: { opacity: 1, y: 0, scale: 1, transition: { duration: 0.3 } }
};
